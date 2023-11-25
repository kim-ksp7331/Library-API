package ksp7331.practice.libraryAPI.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ksp7331.practice.libraryAPI.member.dto.MemberControllerDTO;
import ksp7331.practice.libraryAPI.member.dto.MemberServiceDTO;
import ksp7331.practice.libraryAPI.member.mapper.MemberMapper;
import ksp7331.practice.libraryAPI.member.service.LibraryMemberService;
import ksp7331.practice.libraryAPI.util.UriCreator;
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
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(MemberController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LibraryMemberService libraryMemberService;
    @MockBean
    private MemberMapper memberMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void postMember() throws Exception {
        // given
        long libraryMemberId = 1L;
        String name = "kim";
        long libraryId = 1L;
        String phoneNumber = "010-0000-0000";

        Map<String, Object> post = new HashMap<>();
        post.put("libraryMemberId", libraryMemberId);
        post.put("name", name);
        post.put("libraryId", libraryId);
        post.put("phoneNumber", phoneNumber);

        String content = objectMapper.writeValueAsString(post);
        Long id = 1L;
        BDDMockito.given(memberMapper.postToCreateParam(Mockito.any(MemberControllerDTO.Post.class)))
                .willReturn(MemberServiceDTO.CreateParam.builder().build());
        BDDMockito.given(libraryMemberService.createLibraryMember(Mockito.any(MemberServiceDTO.CreateParam.class))).willReturn(id);
        String url = "/members";

        // when
        ResultActions actions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));
        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(header().string("location", UriCreator.createUri(url, id).toString()))
                .andDo(document(
                        "post-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("libraryMemberId").type(JsonFieldType.NUMBER).description("도서관회원 id, 기존 회원이 다른 도서관에 추가 등록할 시에 사용"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("회원 이름"),
                                fieldWithPath("libraryId").type(JsonFieldType.NUMBER).description("도서관 id"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("전화번호")
                        ), responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("도서관 회원 리소스의 엔드포인트: /members/{library-member-id}")
                        )
                ));

    }

    @Test
    void deleteMember() throws Exception {
        // given
        Long libraryMemberId = 1L;
        String url = "/members";

        // when
        ResultActions actions = mockMvc.perform(
                delete(url + "/{library-member-id}", libraryMemberId)
        );
        // then
        actions
                .andExpect(status().isNoContent())
                .andDo(document(
                        "delete-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("library-member-id").description("도서관회원 id")
                        )
                ));
    }
}