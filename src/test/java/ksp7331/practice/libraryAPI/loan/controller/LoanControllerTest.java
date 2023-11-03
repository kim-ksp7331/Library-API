package ksp7331.practice.libraryAPI.loan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ksp7331.practice.libraryAPI.book.controller.BookController;
import ksp7331.practice.libraryAPI.library.mapper.LibraryMapper;
import ksp7331.practice.libraryAPI.library.service.LibraryService;
import ksp7331.practice.libraryAPI.loan.dto.LoanControllerDTO;
import ksp7331.practice.libraryAPI.loan.dto.LoanServiceDTO;
import ksp7331.practice.libraryAPI.loan.mapper.LoanMapper;
import ksp7331.practice.libraryAPI.loan.service.LoanService;
import ksp7331.practice.libraryAPI.util.UriCreator;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanController.class)
@MockBean(JpaMetamodelMappingContext.class)
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LoanService loanService;
    @MockBean
    private LoanMapper loanMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Test
    void postLoan() throws Exception {
        // given
        List<Long> bookIds = List.of(1L, 2L);
        LoanControllerDTO.Post post = LoanControllerDTO.Post.builder().bookIds(bookIds).build();
        Long libraryMemberId = 1L;
        Long loanId = 1L;

        String content = objectMapper.writeValueAsString(post);
        BDDMockito.given(loanService.createLoan(Mockito.any(LoanServiceDTO.CreateParam.class))).willReturn(loanId);
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
                .andExpect(header().string("location", String.format("/members/%d/loan/%d", libraryMemberId, loanId)));
    }

    @Test
    void getLoan() throws Exception {
        // given
        long loanId = 1L;
        long libraryMemberId = 1L;
        String memberName = "kim";
        String libraryName = "lib1";
        LocalDateTime createdDate = LocalDateTime.now();

        int repeat = 2;
        String bookName = "book";
        String author = "author";
        List<LoanControllerDTO.Response.Book> books = LongStream.rangeClosed(1, repeat).mapToObj(i -> LoanControllerDTO.Response.Book.builder()
                .name(bookName + i).author(author + i).build()).collect(Collectors.toList());

        LoanServiceDTO.Result result = LoanServiceDTO.Result.builder().build();
        LoanControllerDTO.Response response = LoanControllerDTO.Response.builder()
                .id(loanId)
                .libraryMemberId(libraryMemberId)
                .memberName(memberName)
                .libraryName(libraryName)
                .createdDate(createdDate)
                .books(books)
                .build();

        BDDMockito.given(loanService.findLoan(Mockito.anyLong())).willReturn(result);
        BDDMockito.given(loanMapper.serviceDTOToControllerDTO(result)).willReturn(response);

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
                .andExpect(jsonPath("$.createdDate").value(createdDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))))
                .andExpect(jsonPath("$.books[*].name").value(everyItem(is(startsWith(bookName)))))
                .andExpect(jsonPath("$.books[*].author").value(everyItem(is(startsWith(author)))));
    }
}