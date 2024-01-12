package ksp7331.practice.libraryAPI.member.infrastructure;

import ksp7331.practice.libraryAPI.member.infrastructure.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
}
