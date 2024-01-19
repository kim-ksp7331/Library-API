package ksp7331.practice.libraryAPI.book.infrastructure;

import ksp7331.practice.libraryAPI.book.infrastructure.entity.LibraryBookEntity;

import java.util.List;

public interface LibraryBookCustomRepository {
    List<LibraryBookEntity> findByLibraryIdAndBookIds(Long libraryId, List<Long> bookIds);
}
