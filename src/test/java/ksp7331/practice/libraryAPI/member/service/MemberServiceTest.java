package ksp7331.practice.libraryAPI.member.service;

import ksp7331.practice.libraryAPI.member.domain.Member;
import ksp7331.practice.libraryAPI.member.service.port.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private MemberService memberService;

    @DisplayName("member 엔티티 생성")
    @Test
    void createMember() {
        // given
        String name = "kim";
        Member member = Member.builder().name(name).build();

        BDDMockito.given(memberRepository.create(Mockito.any(Member.class))).willReturn(member);

        // when
        Member result = memberService.createMember(name);

        // then
        assertThat(result).isEqualTo(member);
    }


}