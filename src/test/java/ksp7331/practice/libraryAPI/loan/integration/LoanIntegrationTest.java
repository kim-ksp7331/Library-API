package ksp7331.practice.libraryAPI.loan.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ksp7331.practice.libraryAPI.IntegrationTest;
import ksp7331.practice.libraryAPI.book.entity.Book;
import ksp7331.practice.libraryAPI.config.DbTestInitializer;
import ksp7331.practice.libraryAPI.loan.dto.LoanControllerDTO;
import ksp7331.practice.libraryAPI.loan.entity.LoanBook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class LoanIntegrationTest extends IntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DbTestInitializer dbTestInitializer;
    @Autowired
    private ObjectMapper objectMapper;
    @Test
    @DisplayName("도서 대출")
    void postLoan() throws Exception {
        // given
        List<Long> bookIds = List.of(1L, 2L);
        LoanControllerDTO.Post post = LoanControllerDTO.Post.builder().bookIds(bookIds).build();
        Long libraryMemberId = 1L;

        String content = objectMapper.writeValueAsString(post);
        String urlTemplates = "/members/{library-member-id}/loan";

        // when
        ResultActions actions = mockMvc.perform(
                post(urlTemplates, libraryMemberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(header().string("location", is(startsWith(String.format("/members/%d/loan", libraryMemberId)))));
    }
    @Test
    @DisplayName("현재 존재하지 않는 도서 대출")
    void postLoanWhenBooksNotExists() throws Exception {
        // given
        List<Long> bookIds = List.of(1L, 2L);
        LoanControllerDTO.Post post = LoanControllerDTO.Post.builder().bookIds(bookIds).build();
        Long libraryMemberId = 2L;

        String content = objectMapper.writeValueAsString(post);
        String urlTemplates = "/members/{library-member-id}/loan";

        // when
        ResultActions actions = mockMvc.perform(
                post(urlTemplates, libraryMemberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        // then
        actions
                .andExpect(status().isNotFound());
    }
    @Test
    @DisplayName("도서 대출 권수 초과")
    void postLoanWithExceededBooks() throws Exception {
        // given
        List<Long> bookIds = List.of(1L, 2L, 3L, 4L, 5L, 6L);
        LoanControllerDTO.Post post = LoanControllerDTO.Post.builder().bookIds(bookIds).build();
        Long libraryMemberId = 1L;

        String content = objectMapper.writeValueAsString(post);
        String urlTemplates = "/members/{library-member-id}/loan";

        // when
        ResultActions actions = mockMvc.perform(
                post(urlTemplates, libraryMemberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        // then
        actions
                .andExpect(status().isForbidden());
    }


    @Test
    @DisplayName("도서 반납")
    void postReturnBook() throws Exception {
        // given
        long loanId = 1L;
        long libraryMemberId = 2L;
        List<Long> bookIds = List.of(1L);
        LoanControllerDTO.ReturnPost returnPost = LoanControllerDTO.ReturnPost.builder().bookIds(bookIds).build();

        String content = objectMapper.writeValueAsString(returnPost);
        String urlTemplates = "/members/{library-member-id}/loan/{loan-id}";

        // when
        ResultActions actions = mockMvc.perform(
                post(urlTemplates, libraryMemberId, loanId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(loanId))
                .andExpect(jsonPath("$.libraryMemberId").value(libraryMemberId))
                .andExpect(jsonPath("$.books[0].state").value(LoanBook.State.RETURN.name()))
                .andExpect(jsonPath("$.books[1].state").value(LoanBook.State.LOANED.name()));

    }

    @Test
    @DisplayName("대출 기록 조회")
    void getLoan() throws Exception {
        // given
        long loanId = 1L;
        long libraryMemberId = 2L;
        String memberName = dbTestInitializer.getMembers().get(0).getName();
        String libraryName = dbTestInitializer.getLibraries().get(1).getName();
        Book book1 = dbTestInitializer.getBooks().get(0);
        Book book2 = dbTestInitializer.getBooks().get(1);



        String urlTemplates = "/members/{library-member-id}/loan/{loan-id}";


        // when
        ResultActions actions = mockMvc.perform(
                get(urlTemplates, libraryMemberId, loanId)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(loanId))
                .andExpect(jsonPath("$.libraryMemberId").value(libraryMemberId))
                .andExpect(jsonPath("$.memberName").value(memberName))
                .andExpect(jsonPath("$.libraryName").value(libraryName))
                .andExpect(jsonPath("$.books[0].name").value(book1.getName()))
                .andExpect(jsonPath("$.books[0].author").value(book1.getAuthor()))
                .andExpect(jsonPath("$.books[1].name").value(book2.getName()))
                .andExpect(jsonPath("$.books[1].author").value(book2.getAuthor()))
                .andExpect(jsonPath("$.books[*].state").value(everyItem(is(LoanBook.State.LOANED.name()))));

    }
}
