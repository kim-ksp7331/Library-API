package ksp7331.practice.libraryAPI.book.service;

import ksp7331.practice.libraryAPI.book.entity.Book;
import ksp7331.practice.libraryAPI.book.entity.LibraryBook;
import ksp7331.practice.libraryAPI.book.repository.LibraryBookRepository;
import ksp7331.practice.libraryAPI.library.entity.Library;
import ksp7331.practice.libraryAPI.library.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LibraryBookService {
    private final LibraryBookRepository libraryBookRepository;
    private final LibraryService libraryService;
    public void createLibraryBook(Book book, Long libraryId) {
        Library library = libraryService.findVerifiedLibrary(libraryId);
        LibraryBook libraryBook = LibraryBook.builder()
                .book(book)
                .library(library)
                .build();
        libraryBookRepository.save(libraryBook);
    }

    public void verifyExistBook(Library library, Book book) {

    }
}
