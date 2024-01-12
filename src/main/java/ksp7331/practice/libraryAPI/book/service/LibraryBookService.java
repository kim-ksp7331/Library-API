package ksp7331.practice.libraryAPI.book.service;

import ksp7331.practice.libraryAPI.book.domain.Book;
import ksp7331.practice.libraryAPI.book.domain.LibraryBook;
import ksp7331.practice.libraryAPI.book.service.port.LibraryBookRepository;
import ksp7331.practice.libraryAPI.library.domain.Library;
import ksp7331.practice.libraryAPI.library.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class LibraryBookService {
    private final LibraryBookRepository libraryBookRepository;
    private final LibraryService libraryService;
    public void createLibraryBook(Book book, Long libraryId) {
        Library library = libraryService.getById(libraryId);
        LibraryBook libraryBook = LibraryBook.builder().library(library).book(book).build();
        libraryBookRepository.create(libraryBook);
    }

    public List<LibraryBook> findExistBookInLibrary(Long libraryId, List<Long> bookIds) {
        List<LibraryBook> libraryBooks = libraryBookRepository.findByLibraryIdAndBookIds(libraryId, bookIds);
        return libraryBooks;
    }
}
