package ksp7331.practice.libraryAPI.member.repository;

import ksp7331.practice.libraryAPI.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
