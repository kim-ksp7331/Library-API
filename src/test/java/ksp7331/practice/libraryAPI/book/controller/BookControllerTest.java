package ksp7331.practice.libraryAPI.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ksp7331.practice.libraryAPI.book.dto.BookControllerDTO;
import ksp7331.practice.libraryAPI.book.dto.BookServiceDTO;
import ksp7331.practice.libraryAPI.book.mapper.BookMapper;
import ksp7331.practice.libraryAPI.book.service.BookService;
import ksp7331.practice.libraryAPI.common.dto.MultiResponseDTO;
import ksp7331.practice.libraryAPI.library.dto.LibraryControllerDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
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
                .andExpect(header().string("location", is(endsWith(bookId.toString()))))
                .andDo(document(
                        "post-new-book",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("library-id").description("도서관 id")
                        ), requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("책 이름"),
                                fieldWithPath("author").type(JsonFieldType.STRING).description("책 저자"),
                                fieldWithPath("publisher").type(JsonFieldType.STRING).description("출판사")
                        ), responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("도서 리소스의 엔드포인트: /books/{book-id}")
                        )
                ));
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
                .andExpect(status().isOk())
                .andDo(document(
                        "post-book",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("library-id").description("도서관 id"),
                                parameterWithName("book-id").description("도서 id")
                        )
                ));
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
                .andExpect(jsonPath("$.libraries[0].name").value(libraryName))
                .andDo(document(
                        "get-book",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("book-id").description("도서 id")
                        ), responseFields(
                                fieldWithPath("bookId").type(JsonFieldType.NUMBER).description("도서 id"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("도서 이름"),
                                fieldWithPath("author").type(JsonFieldType.STRING).description("도서 저자"),
                                fieldWithPath("publisher").type(JsonFieldType.STRING).description("출판사"),
                                fieldWithPath("libraries").type(JsonFieldType.ARRAY).description("도서가 소장된 도서관 목록"),
                                fieldWithPath("libraries.[].id").type(JsonFieldType.NUMBER).description("도서관 id"),
                                fieldWithPath("libraries.[].name").type(JsonFieldType.STRING).description("도서관 이름")
                        )
                ));
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
        String name = "book";
        String author = "author";
        String publisher = "publisher";

        long libraryId = 1L;
        String libraryName = "lib1";

        LibraryControllerDTO.Response libraryResponse = LibraryControllerDTO.Response.builder()
                .id(libraryId).name(libraryName).build();

        Pageable pageable = PageRequest.of(page - 1, size);
        List<BookControllerDTO.Response> bookList = LongStream.rangeClosed(1, 3).mapToObj(i -> BookControllerDTO.Response.builder()
                .bookId(i)
                .name(name + i)
                .author(author + i)
                .publisher(publisher + i)
                .libraries(List.of(libraryResponse))
                .build()).collect(Collectors.toList());
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
                .andExpect(jsonPath("$.data[*].name").value(everyItem(is(startsWith(name)))))
                .andExpect(jsonPath("$.data[*].author").value(everyItem(is(startsWith(author)))))
                .andExpect(jsonPath("$.data[*].publisher").value(everyItem(is(startsWith(publisher)))))
                .andExpect(jsonPath("$.data[*].libraries[0].id").value(everyItem(is((int)libraryId))))
                .andExpect(jsonPath("$.data[*].libraries[0].name").value(everyItem(is(libraryName))))
                .andExpect(jsonPath("$.pageInfo.page").value(page))
                .andExpect(jsonPath("$.pageInfo.size").value(size))
                .andDo(document(
                        "get-books",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("페이지, 1부터 시작"),
                                parameterWithName("size").description("한 페이지당 도서 수")
                        ), responseFields(
                                fieldWithPath("data.[].bookId").type(JsonFieldType.NUMBER).description("도서 id"),
                                fieldWithPath("data.[].name").type(JsonFieldType.STRING).description("도서 이름"),
                                fieldWithPath("data.[].author").type(JsonFieldType.STRING).description("도서 저자"),
                                fieldWithPath("data.[].publisher").type(JsonFieldType.STRING).description("출판사"),
                                fieldWithPath("data.[].libraries").type(JsonFieldType.ARRAY).description("도서가 소장된 도서관 목록"),
                                fieldWithPath("data.[].libraries.[].id").type(JsonFieldType.NUMBER).description("도서관 id"),
                                fieldWithPath("data.[].libraries.[].name").type(JsonFieldType.STRING).description("도서관 이름"),
                                fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("페이지 번호"),
                                fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("한 페이지당 도서 수"),
                                fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 도서 수"),
                                fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수")
                        )
                ));
    }
}