package ksp7331.practice.libraryAPI.member.infrastructure;

import ksp7331.practice.libraryAPI.member.domain.Member;
import ksp7331.practice.libraryAPI.member.service.port.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {
    private final MemberJpaRepository memberJpaRepository;
    @Override
    public Member create(Member member) {
        return memberJpaRepository.save(ksp7331.practice.libraryAPI.member.infrastructure.entity.Member.from(member)).toDomain();
    }

    @Override
    public void delete(Member member) {
        memberJpaRepository.delete(ksp7331.practice.libraryAPI.member.infrastructure.entity.Member.from(member));
    }
}
