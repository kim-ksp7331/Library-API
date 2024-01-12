package ksp7331.practice.libraryAPI.member.infrastructure;

import ksp7331.practice.libraryAPI.member.domain.LibraryMember;
import ksp7331.practice.libraryAPI.member.service.port.LibraryMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LibraryMemberRepositoryImpl implements LibraryMemberRepository {
    private final LibraryMemberJpaRepository libraryMemberJpaRepository;
    @Override
    public LibraryMember create(LibraryMember libraryMember) {
        return libraryMemberJpaRepository.save(ksp7331.practice.libraryAPI.member.infrastructure.entity.LibraryMember.from(libraryMember)).toDomain();
    }

    @Override
    public Optional<LibraryMember> findById(Long id) {
        return libraryMemberJpaRepository.findById(id).map(ksp7331.practice.libraryAPI.member.infrastructure.entity.LibraryMember::toDomain);
    }

    @Override
    public void delete(LibraryMember libraryMember) {
        libraryMemberJpaRepository.delete(ksp7331.practice.libraryAPI.member.infrastructure.entity.LibraryMember.from(libraryMember));
    }
}
