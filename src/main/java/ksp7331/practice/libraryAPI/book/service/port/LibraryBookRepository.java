package ksp7331.practice.libraryAPI.book.service.port;

import ksp7331.practice.libraryAPI.book.domain.LibraryBook;

import java.util.List;

public interface LibraryBookRepository {
    LibraryBook create(LibraryBook libraryBook);
    List<LibraryBook> findByLibraryIdAndBookIds(Long libraryId, List<Long> bookIds);

}

