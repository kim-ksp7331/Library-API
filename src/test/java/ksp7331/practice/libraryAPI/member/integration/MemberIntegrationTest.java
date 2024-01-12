package ksp7331.practice.libraryAPI.member.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import ksp7331.practice.libraryAPI.config.DbTestInitializer;
import ksp7331.practice.libraryAPI.IntegrationTest;
import ksp7331.practice.libraryAPI.member.infrastructure.entity.LibraryMember;
import ksp7331.practice.libraryAPI.member.infrastructure.LibraryMemberJpaRepository;
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
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MemberIntegrationTest extends IntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private LibraryMemberJpaRepository libraryMemberRepository;
    @Autowired
    private DbTestInitializer dbTestInitializer;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("신규 회원 등록")
    @Test
    void postMemberFirstTime() throws Exception {
        // given
        String name = "kim";
        long libraryId = 1L;
        String phoneNumber = "010-0000-0000";

        Map<String, Object> post = new HashMap<>();
        post.put("name", name);
        post.put("libraryId", libraryId);
        post.put("phoneNumber", phoneNumber);

        String content = objectMapper.writeValueAsString(post);
        String url = "/members";

        // when
        ResultActions actions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));
        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(header().string("location", is(startsWith(url))));

        long id = getId(url, actions);
        LibraryMember libraryMember = libraryMemberRepository.findById(id).get();
        assertThat(libraryMember).isNotNull();
        assertThat(libraryMember.getMember().getName()).isEqualTo(name);
        assertThat(libraryMember.getPhones()).anyMatch(phone -> phone.getNumber().equals(phoneNumber));
    }

    @DisplayName("기존 회원 다른 도서관 등록")
    @Test
    void postLibraryMember() throws Exception {
        // given
        long libraryMemberId = 1L;
        long libraryId = 1L;
        String phoneNumber = "010-1111-1111";

        Map<String, Object> post = new HashMap<>();
        post.put("libraryMemberId", libraryMemberId);
        post.put("libraryId", libraryId);
        post.put("phoneNumber", phoneNumber);

        String content = objectMapper.writeValueAsString(post);
        String url = "/members";

        // when
        ResultActions actions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));
        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(header().string("location", is(startsWith(url))));

        long id = getId(url, actions);
        LibraryMember findLibraryMember = libraryMemberRepository.findById(id).get();
        assertThat(findLibraryMember).isNotNull();
        assertThat(findLibraryMember.getMember().getName()).isEqualTo(dbTestInitializer.getMembers().get(0).getName());
        assertThat(findLibraryMember.getPhones()).anyMatch(phone -> phone.getNumber().equals(phoneNumber));

    }


    @DisplayName("회원 탈퇴")
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
        assertThat(libraryMemberRepository.findById(libraryMemberId).isEmpty()).isTrue();
    }
}
