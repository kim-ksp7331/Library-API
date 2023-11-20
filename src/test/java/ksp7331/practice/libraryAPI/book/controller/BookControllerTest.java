package ksp7331.practice.libraryAPI.book.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ksp7331.practice.libraryAPI.book.dto.BookControllerDTO;
import ksp7331.practice.libraryAPI.book.dto.BookServiceDTO;
import ksp7331.practice.libraryAPI.book.mapper.BookMapper;
import ksp7331.practice.libraryAPI.book.service.BookService;
import ksp7331.practice.libraryAPI.library.dto.LibraryControllerDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.RepeatedTest;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@MockBean(JpaMetamodelMappingContext.class)
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;
    @MockBean
    private BookMapper bookMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void postNewBook() throws Exception {
        // given
        String name = "book1";
        String author = "author1";
        String publisher = "publisher1";
        Long libraryId = 1L;
        Long bookId = 12L;
        Map<String, String> post = new HashMap<>();
        post.put("name", name);
        post.put("author", author);
        post.put("publisher", publisher);

        String content = objectMapper.writeValueAsString(post);

        BDDMockito.given(bookMapper.controllerDTOToServiceDTO(Mockito.any(BookControllerDTO.Post.class), Mockito.anyLong()))
                .willReturn(BookServiceDTO.CreateParam.builder().build());
        BDDMockito.given(bookService.createNewBook(Mockito.any(BookServiceDTO.CreateParam.class)))
                .willReturn(bookId);

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

    @Test
    void getBook() throws Exception {
        // given
        Long bookId = 1L;
        String name = "book1";
        String author = "author1";
        String publisher = "publisher1";
        long libraryId = 1L;
        String libraryName = "lib1";

        LibraryControllerDTO.Response libraryResponse = LibraryControllerDTO.Response.builder()
                .id(libraryId).name(libraryName).build();
        BookControllerDTO.Response response = BookControllerDTO.Response.builder()
                .bookId(bookId).name(name).author(author).publisher(publisher).libraries(List.of(libraryResponse)).build();

        BDDMockito.given(bookService.findBook(Mockito.anyLong())).willReturn(BookServiceDTO.Result.builder().build());
        BDDMockito.given(bookMapper.ServiceDTOToControllerDTO(Mockito.any(BookServiceDTO.Result.class)))
                .willReturn(response);

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
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.author").value(author))
                .andExpect(jsonPath("$.publisher").value(publisher))
                .andExpect(jsonPath("$.libraries[0].id").value(libraryId))
                .andExpect(jsonPath("$.libraries[0].name").value(libraryName));
    }
}