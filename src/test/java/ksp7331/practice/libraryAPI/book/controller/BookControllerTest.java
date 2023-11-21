package ksp7331.practice.libraryAPI.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ksp7331.practice.libraryAPI.book.dto.BookControllerDTO;
import ksp7331.practice.libraryAPI.book.dto.BookServiceDTO;
import ksp7331.practice.libraryAPI.book.mapper.BookMapper;
import ksp7331.practice.libraryAPI.book.service.BookService;
import ksp7331.practice.libraryAPI.common.dto.MultiResponseDTO;
import ksp7331.practice.libraryAPI.library.dto.LibraryControllerDTO;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        BDDMockito.given(bookMapper.serviceDTOToControllerDTO(Mockito.any(BookServiceDTO.Result.class)))
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

    @Test
    void getBooks() throws Exception {
        // given
        int page = 1;
        int size = 3;
        String urlTemplate = "/books";

        BookServiceDTO.PageParam param = BookServiceDTO.PageParam.builder().build();
        Page<BookServiceDTO.Result> results = new PageImpl<>(List.of(BookServiceDTO.Result.builder().build()));

        long bookId = 1L;
        String name = "book1";

        Pageable pageable = PageRequest.of(page - 1, size);
        List<BookControllerDTO.Response> bookList = List.of(BookControllerDTO.Response.builder().bookId(bookId).name(name).build());
        Page<BookControllerDTO.Response> responses = new PageImpl<>(bookList, pageable, bookList.size());
        MultiResponseDTO<BookControllerDTO.Response> dto = MultiResponseDTO.<BookControllerDTO.Response>builder()
                .data(responses.getContent()).page(responses).build();

        BDDMockito.given(bookMapper.controllerDTOToServiceDTOForPage(Mockito.any(BookControllerDTO.FindPage.class))).willReturn(param);
        BDDMockito.given(bookService.findBooks(param)).willReturn(results);
        BDDMockito.given(bookMapper.serviceDTOsToControllerDTO(results)).willReturn(dto);

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
                .andExpect(jsonPath("$.data[0].bookId").value(bookId))
                .andExpect(jsonPath("$.data[0].name").value(name))
                .andExpect(jsonPath("$.pageInfo.page").value(page))
                .andExpect(jsonPath("$.pageInfo.size").value(size));
    }
}