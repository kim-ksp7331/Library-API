package ksp7331.practice.libraryAPI.book.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import ksp7331.practice.libraryAPI.IntegrationTest;
import ksp7331.practice.libraryAPI.book.dto.BookControllerDTO;
import ksp7331.practice.libraryAPI.book.dto.BookServiceDTO;
import ksp7331.practice.libraryAPI.book.entity.Book;
import ksp7331.practice.libraryAPI.book.mapper.BookMapper;
import ksp7331.practice.libraryAPI.book.service.BookService;
import ksp7331.practice.libraryAPI.config.DbTestInitializer;
import ksp7331.practice.libraryAPI.library.dto.LibraryControllerDTO;
import ksp7331.practice.libraryAPI.library.entity.Library;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BookIntegrationTest extends IntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DbTestInitializer dbTestInitializer;

    @DisplayName("신규 도서 등록")
    @Test
    void postNewBook() throws Exception {
        // given
        String name = "book1";
        String author = "author1";
        Long libraryId = 1L;
        Map<String, String> post = new HashMap<>();
        post.put("name", name);
        post.put("author", author);

        String content = objectMapper.writeValueAsString(post);
        Long bookId = (long)dbTestInitializer.getBooks().size() + 1;


        String urlTemplate = "/libraries/{library-id}/books";

        // when
        ResultActions actions = mockMvc.perform(
                post(urlTemplate, libraryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(header().string("location", is(endsWith(bookId.toString()))));
    }

    @DisplayName("기존 도서 다른 도서관에 등록")
    @Test
    void postBook() throws Exception {
        // given
        Long libraryId = 2L;
        Long bookId = 5L;

        String urlTemplate = "/libraries/{library-id}/books/{book-id}";

        // when
        ResultActions actions = mockMvc.perform(
                post(urlTemplate, libraryId, bookId)
        );

        // then
        actions
                .andExpect(status().isOk());
    }
    @DisplayName("id를 통해 도서 조회")
    @Test
    void getBook() throws Exception {
        // given
        Long bookId = 1L;
        Book book = dbTestInitializer.getBooks().get(0);
        List<Library> libraries = dbTestInitializer.getLibraries();


        String urlTemplate = "/books/{book-id}";

        // when
        ResultActions actions = mockMvc.perform(
                get(urlTemplate, bookId)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(bookId))
                .andExpect(jsonPath("$.name").value(book.getName()))
                .andExpect(jsonPath("$.author").value(book.getAuthor()));
        for (int i = 0; i < 2; i++) {
            actions
                    .andExpect(jsonPath("$.libraries[%d].id", i).value(libraries.get(i).getId()))
                    .andExpect(jsonPath("$.libraries[%d].name", i).value(libraries.get(i).getName()));
        }
    }
}
