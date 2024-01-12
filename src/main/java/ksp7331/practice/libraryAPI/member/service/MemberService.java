package ksp7331.practice.libraryAPI.member.service;

import ksp7331.practice.libraryAPI.member.domain.Member;
import ksp7331.practice.libraryAPI.member.service.port.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    public Member createMember(String name) {
        Member member = Member.from(name);
        return memberRepository.create(member);
    }

    public void deleteMember(Member member) {
        memberRepository.delete(member);
    }


}
