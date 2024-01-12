package ksp7331.practice.libraryAPI.book.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import ksp7331.practice.libraryAPI.IntegrationTest;
import ksp7331.practice.libraryAPI.book.infrastructure.entity.Book;
import ksp7331.practice.libraryAPI.config.DbTestInitializer;
import ksp7331.practice.libraryAPI.library.infrastructure.entity.Library;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
        String publisher = "publisher1";
        Long libraryId = 1L;
        Map<String, String> post = new HashMap<>();
        post.put("name", name);
        post.put("author", author);
        post.put("publisher", publisher);

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

    @DisplayName("기존 도서 추가 등록")
    @Test
    void postBook() throws Exception {
        // given
        Long libraryId = 2L;
        Long bookId = 1L;

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
                .andExpect(jsonPath("$.author").value(book.getAuthor()))
                .andExpect(jsonPath("$.publisher").value(book.getPublisher()));
        for (int i = 0; i < 2; i++) {
            actions
                    .andExpect(jsonPath("$.libraries[%d].id", i).value(libraries.get(i).getId()))
                    .andExpect(jsonPath("$.libraries[%d].name", i).value(libraries.get(i).getName()));
        }
    }
    @Test
    @DisplayName("페이지네이션을 통한 도서 조회")
    void getBooks() throws Exception {
        // given
        int page = 1;
        int size = 3;
        String urlTemplate = "/books";
        String[] bookNames = dbTestInitializer.getBooks()
                .stream().map(b -> b.getName()).sorted().limit(size).toArray(String[]::new);


        // when
        ResultActions actions = mockMvc.perform(
                get(urlTemplate)
                        .queryParam("page", String.valueOf(page))
                        .queryParam("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageInfo.page").value(page))
                .andExpect(jsonPath("$.pageInfo.size").value(size));
        for (int i = 0; i < 3; i++) {
            actions.andExpect(jsonPath("$.data[%d].name", i).value(bookNames[i]));
        }
    }
}
