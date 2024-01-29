package ksp7331.practice.libraryAPI.member.infrastructure.entity;

import ksp7331.practice.libraryAPI.member.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberEntityTest {

    @Test
    void from() {
        // given
        Member domain = Member.builder().id(4L).name("kim").build();

        // when
        MemberEntity member = MemberEntity.from(domain);

        // then
        assertThat(member.getId()).isEqualTo(4L);
        assertThat(member.getName()).isEqualTo("kim");
    }

    @Test
    void toDomain() {
        // given
        MemberEntity entity = MemberEntity.builder().id(3L).name("kim").build();

        // when
        Member member = entity.toDomain();

        // then
        assertThat(member.getId()).isEqualTo(3L);
        assertThat(member.getName()).isEqualTo("kim");
    }
}