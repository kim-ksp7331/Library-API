package ksp7331.practice.libraryAPI.member.service.port;

import ksp7331.practice.libraryAPI.member.domain.LibraryMember;

import java.util.Optional;

public interface LibraryMemberRepository {
    LibraryMember create(LibraryMember libraryMember);

    Optional<LibraryMember> findById(Long id);

    void delete(LibraryMember libraryMember);
}
