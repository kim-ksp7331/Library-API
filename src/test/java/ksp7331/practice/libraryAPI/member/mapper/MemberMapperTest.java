package ksp7331.practice.libraryAPI.member.mapper;

import ksp7331.practice.libraryAPI.member.dto.MemberControllerDTO;
import ksp7331.practice.libraryAPI.member.dto.MemberServiceDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberMapperTest {

    private MemberMapper memberMapper = new MemberMapper();
    @Test
    void postToCreateParam() {
        // given
        long libraryMemberId = 1L;
        String name = "kim";
        String phoneNumber = "010-0000-0000";
        long libraryId = 2L;
        MemberControllerDTO.Post post = MemberControllerDTO.Post
                .builder()
                .libraryMemberId(libraryMemberId)
                .name(name)
                .phoneNumber(phoneNumber)
                .libraryId(libraryId)
                .build();

        // when
        MemberServiceDTO.CreateParam result = memberMapper.postToCreateParam(post);

        // then
        assertThat(result.getLibraryMemberId()).isEqualTo(libraryMemberId);
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(result.getLibraryId()).isEqualTo(libraryId);
    }
}