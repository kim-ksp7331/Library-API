package ksp7331.practice.libraryAPI.book.repository;

import ksp7331.practice.libraryAPI.book.entity.LibraryBook;

import java.util.List;

public interface LibraryBookCustomRepository {
    List<LibraryBook> findByLibraryIdAndBookIds(Long libraryId, List<Long> bookIds);
}
