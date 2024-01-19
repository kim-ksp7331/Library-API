package ksp7331.practice.libraryAPI.library.infrastructure;

import ksp7331.practice.libraryAPI.library.domain.Library;
import ksp7331.practice.libraryAPI.library.infrastructure.entity.LibraryEntity;
import ksp7331.practice.libraryAPI.library.service.port.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class LibraryRepositoryImpl implements LibraryRepository {
    private final LibraryJpaRepository libraryJpaRepository;
    @Override
    public Library create(Library library) {
        return libraryJpaRepository.save(LibraryEntity.from(library)).toDomain();
    }

    @Override
    public List<Library> findAll() {
        return libraryJpaRepository.findAll().stream().map(LibraryEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Library> findById(Long id) {
        return libraryJpaRepository.findById(id).map(LibraryEntity::toDomain);
    }
}
