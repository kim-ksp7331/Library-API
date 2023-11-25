package ksp7331.practice.libraryAPI.member.repository;

import ksp7331.practice.libraryAPI.member.entity.LibraryMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryMemberRepository extends JpaRepository<LibraryMember, Long> {
}
