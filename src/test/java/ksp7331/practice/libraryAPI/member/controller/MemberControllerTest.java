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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(MemberController.class)
@MockBean(JpaMetamodelMappingContext.class)
class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LibraryMemberService libraryMemberService;
    @MockBean
    private MemberMapper memberMapper;

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

        String content = new ObjectMapper().writeValueAsString(post);
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
                .andExpect(header().string("location", UriCreator.createUri(url, id).toString()));

    }

    @Test
    void deleteMember() throws Exception {
        // given
        Long libraryMemberId = 1L;
        String url = "/members";

        // when
        ResultActions actions = mockMvc.perform(
                delete(url + "/{id}", libraryMemberId)
        );
        // then
        actions
                .andExpect(status().isNoContent());
    }
}