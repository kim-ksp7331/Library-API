package ksp7331.practice.libraryAPI.book.infrastructure;

import ksp7331.practice.libraryAPI.book.infrastructure.entity.LibraryBook;

import java.util.List;

public interface LibraryBookCustomRepository {
    List<LibraryBook> findByLibraryIdAndBookIds(Long libraryId, List<Long> bookIds);
}
