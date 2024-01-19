package ksp7331.practice.libraryAPI.book.infrastructure;

import ksp7331.practice.libraryAPI.book.domain.LibraryBook;
import ksp7331.practice.libraryAPI.book.infrastructure.entity.LibraryBookEntity;
import ksp7331.practice.libraryAPI.book.service.port.LibraryBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class LibraryBookRepositoryImpl implements LibraryBookRepository {
    private final LibraryBookJpaRepository libraryBookJpaRepository;
    private final BookJpaRepository bookJpaRepository;
    @Override
    public LibraryBook create(LibraryBook libraryBook) {
        return libraryBookJpaRepository.save(LibraryBookEntity.from(libraryBook)).toDomain();
    }

    @Override
    public List<LibraryBook> findByLibraryIdAndBookIds(Long libraryId, List<Long> bookIds) {
        return libraryBookJpaRepository.findByLibraryIdAndBookIds(libraryId, bookIds).stream()
                .map(LibraryBookEntity::toDomain).collect(Collectors.toList());
    }
}
