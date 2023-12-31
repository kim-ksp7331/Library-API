package ksp7331.practice.libraryAPI.loan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ksp7331.practice.libraryAPI.loan.dto.LoanControllerDTO;
import ksp7331.practice.libraryAPI.loan.dto.LoanServiceDTO;
import ksp7331.practice.libraryAPI.loan.entity.LoanBook;
import ksp7331.practice.libraryAPI.loan.mapper.LoanMapper;
import ksp7331.practice.libraryAPI.loan.service.LoanService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
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
                .andExpect(header().string("location", String.format("/members/%d/loan/%d", libraryMemberId, loanId)))
                .andDo(document(
                        "post-loan",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("library-member-id").description("도서관 회원 id")
                        ), requestFields(
                                fieldWithPath("bookIds").type(JsonFieldType.ARRAY).description("도서 id 리스트")
                        ), responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("대출 리소스의 엔드포인트: /members/{library-member-id}/loan/{loan-id}")
                        )
                ));
    }

    @Test
    void postReturnBook() throws Exception {
        // given
        List<Long> bookIds = List.of(1L, 2L);
        Long loanId = 1L;
        Long libraryMemberId = 1L;
        String memberName = "kim";
        String libraryName = "lib1";
        LocalDateTime createdDate = LocalDateTime.now();


        long bookId = 1L;
        String bookName = "book1";
        LocalDateTime returnDate = LocalDateTime.now();
        String author = "author1";
        String publisher = "publisher1";
        LoanControllerDTO.Response.Book book = LoanControllerDTO.Response.Book.builder()
                .id(bookId)
                .name(bookName)
                .returnDate(returnDate)
                .author(author)
                .publisher(publisher)
                .state(LoanBook.State.LOANED.name())
                .build();

        LoanControllerDTO.ReturnPost returnPost = LoanControllerDTO.ReturnPost.builder().bookIds(bookIds).build();
        LoanServiceDTO.Result result = LoanServiceDTO.Result.builder().build();
        LoanControllerDTO.Response response = LoanControllerDTO.Response.builder()
                .id(loanId)
                .libraryMemberId(libraryMemberId)
                .memberName(memberName)
                .libraryName(libraryName)
                .createdDate(createdDate)
                .books(List.of(book))
                .build();


        String content = objectMapper.writeValueAsString(returnPost);
        BDDMockito.given(loanService.returnBook(Mockito.any(LoanServiceDTO.ReturnBookParam.class))).willReturn(result);
        BDDMockito.given(loanMapper.serviceDTOToControllerDTO(result)).willReturn(response);

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
                .andExpect(jsonPath("$.memberName").value(memberName))
                .andExpect(jsonPath("$.libraryName").value(libraryName))
                .andDo(document(
                        "post-return-book",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("library-member-id").description("도서관 회원 id"),
                                parameterWithName("loan-id").description("대출 id")
                        ), requestFields(
                                fieldWithPath("bookIds").type(JsonFieldType.ARRAY).description("도서 id 리스트")
                        ), responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("대출 id"),
                                fieldWithPath("libraryMemberId").type(JsonFieldType.NUMBER).description("도서관 회원 id"),
                                fieldWithPath("memberName").type(JsonFieldType.STRING).description("회원 이름"),
                                fieldWithPath("libraryName").type(JsonFieldType.STRING).description("도서관 이름"),
                                fieldWithPath("createdDate").type(JsonFieldType.STRING).description("대출 시각"),
                                fieldWithPath("books").type(JsonFieldType.ARRAY).description("대출 도서 목록"),
                                fieldWithPath("books.[].id").type(JsonFieldType.NUMBER).description("도서 id"),
                                fieldWithPath("books.[].returnDate").type(JsonFieldType.STRING).description("반납 시각"),
                                fieldWithPath("books.[].name").type(JsonFieldType.STRING).description("도서 이름"),
                                fieldWithPath("books.[].author").type(JsonFieldType.STRING).description("도서 저자"),
                                fieldWithPath("books.[].publisher").type(JsonFieldType.STRING).description("출판사"),
                                fieldWithPath("books.[].state").type(JsonFieldType.STRING).description("도서 대출 상태")
                        )
                ));
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
        String publisher = "publisher";
        LocalDateTime returnDate = LocalDateTime.now();
        String bookState = LoanBook.State.LOANED.name();
        List<LoanControllerDTO.Response.Book> books = LongStream.rangeClosed(1, repeat).mapToObj(i -> LoanControllerDTO.Response.Book.builder()
                .id(i).name(bookName + i).state(bookState).author(author + i).publisher(publisher + i).returnDate(returnDate).build()).collect(Collectors.toList());

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
                .andExpect(jsonPath("$.books[*].author").value(everyItem(is(startsWith(author)))))
                .andExpect(jsonPath("$.books[*].publisher").value(everyItem(is(startsWith(publisher)))))
                .andExpect(jsonPath("$.books[*].state").value(everyItem(is(bookState))))
                .andDo(document(
                        "get-loan",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("library-member-id").description("도서관 회원 id"),
                                parameterWithName("loan-id").description("대출 id")
                        ), responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("대출 id"),
                                fieldWithPath("libraryMemberId").type(JsonFieldType.NUMBER).description("도서관 회원 id"),
                                fieldWithPath("memberName").type(JsonFieldType.STRING).description("회원 이름"),
                                fieldWithPath("libraryName").type(JsonFieldType.STRING).description("도서관 이름"),
                                fieldWithPath("createdDate").type(JsonFieldType.STRING).description("대출 시각"),
                                fieldWithPath("books").type(JsonFieldType.ARRAY).description("대출 도서 목록"),
                                fieldWithPath("books.[].id").type(JsonFieldType.NUMBER).description("도서 id"),
                                fieldWithPath("books.[].returnDate").type(JsonFieldType.STRING).description("반납 시각"),
                                fieldWithPath("books.[].name").type(JsonFieldType.STRING).description("도서 이름"),
                                fieldWithPath("books.[].author").type(JsonFieldType.STRING).description("도서 저자"),
                                fieldWithPath("books.[].publisher").type(JsonFieldType.STRING).description("출판사"),
                                fieldWithPath("books.[].state").type(JsonFieldType.STRING).description("도서 대출 상태")
                        )
                ));
    }

    @Test
    void getLoanByMonth() throws Exception {
        // given
        Long loanId = 1L;
        Long libraryMemberId = 1L;
        String memberName = "kim";
        String libraryName = "lib1";
        int year = 2023;
        int month = 11;
        LocalDateTime createdDate = LocalDateTime.now();

        int repeat = 2;
        String bookName = "book";
        String author = "author";
        String publisher = "publisher";
        LocalDateTime returnDate = LocalDateTime.now();
        String bookState = LoanBook.State.LOANED.name();
        List<LoanControllerDTO.Response.Book> books = LongStream.rangeClosed(1, repeat).mapToObj(i -> LoanControllerDTO.Response.Book.builder()
                .id(i).name(bookName + i).state(bookState).author(author + i).publisher(publisher + i).returnDate(returnDate).build()).collect(Collectors.toList());

        List<LoanServiceDTO.Result> results = List.of(LoanServiceDTO.Result.builder().build());
        List<LoanControllerDTO.Response> responses = List.of(LoanControllerDTO.Response.builder()
                .id(loanId)
                .libraryMemberId(libraryMemberId)
                .memberName(memberName)
                .libraryName(libraryName)
                .createdDate(createdDate)
                .books(books)
                .build());


        BDDMockito.given(loanService.findLoanByMonth(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt())).willReturn(results);
        BDDMockito.given(loanMapper.serviceDTOsToControllerDTOs(results)).willReturn(responses);

        String urlTemplates = "/members/{library-member-id}/loan";

        // when
        ResultActions actions = mockMvc.perform(
                get(urlTemplates, libraryMemberId)
                        .param("year", String.valueOf(year))
                        .param("month", String.valueOf(month))
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(loanId))
                .andExpect(jsonPath("$[0].libraryMemberId").value(libraryMemberId))
                .andExpect(jsonPath("$[0].memberName").value(memberName))
                .andExpect(jsonPath("$[0].libraryName").value(libraryName))
                .andDo(document(
                        "get-loans-by-month",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("library-member-id").description("도서관 회원 id")
                        ),
                        requestParameters(
                                parameterWithName("year").description("연도"),
                                parameterWithName("month").description("달")
                        )
                        , responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("대출 id"),
                                fieldWithPath("[].libraryMemberId").type(JsonFieldType.NUMBER).description("도서관 회원 id"),
                                fieldWithPath("[].memberName").type(JsonFieldType.STRING).description("회원 이름"),
                                fieldWithPath("[].libraryName").type(JsonFieldType.STRING).description("도서관 이름"),
                                fieldWithPath("[].createdDate").type(JsonFieldType.STRING).description("대출 시각"),
                                fieldWithPath("[].books").type(JsonFieldType.ARRAY).description("대출 도서 목록"),
                                fieldWithPath("[].books.[].id").type(JsonFieldType.NUMBER).description("도서 id"),
                                fieldWithPath("[].books.[].returnDate").type(JsonFieldType.STRING).description("반납 시각"),
                                fieldWithPath("[].books.[].name").type(JsonFieldType.STRING).description("도서 이름"),
                                fieldWithPath("[].books.[].author").type(JsonFieldType.STRING).description("도서 저자"),
                                fieldWithPath("[].books.[].publisher").type(JsonFieldType.STRING).description("출판사"),
                                fieldWithPath("[].books.[].state").type(JsonFieldType.STRING).description("도서 대출 상태")
                        )
                ));
    }
}