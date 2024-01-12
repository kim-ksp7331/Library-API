package ksp7331.practice.libraryAPI.member.infrastructure;

import ksp7331.practice.libraryAPI.member.infrastructure.entity.LibraryMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryMemberJpaRepository extends JpaRepository<LibraryMember, Long> {
}
