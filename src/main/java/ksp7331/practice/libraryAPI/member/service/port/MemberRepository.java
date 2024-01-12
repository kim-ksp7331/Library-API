package ksp7331.practice.libraryAPI.member.service.port;

import ksp7331.practice.libraryAPI.member.domain.Member;

public interface MemberRepository {
    Member create(Member member);

    void delete(Member member);
}
