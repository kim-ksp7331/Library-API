package ksp7331.practice.libraryAPI.book.service;

import ksp7331.practice.libraryAPI.book.domain.Book;
import ksp7331.practice.libraryAPI.book.dto.*;
import ksp7331.practice.libraryAPI.book.service.port.BookRepository;
import ksp7331.practice.libraryAPI.exception.BusinessLogicException;
import ksp7331.practice.libraryAPI.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final LibraryBookService libraryBookService;

    public Long createNewBook(BookCreate bookCreate) {
        Book book = bookCreate.toDomain();
        Book savedBook = bookRepository.create(book);
        libraryBookService.createLibraryBook(savedBook, bookCreate.getLibraryId());
        return savedBook.getId();
    }

    public void addBookToLibrary(LibraryBookCreate libraryBookCreate) {
        Book book = getByIdInternal(libraryBookCreate.getBookId());
        libraryBookService.createLibraryBook(book, libraryBookCreate.getLibraryId());
    }

    public Book getById(Long bookId) {
        Book book = getByIdInternal(bookId);
        return book;
    }

    public Page<Book> findAll(BookPageCreate dto) {
        Pageable pageable = PageRequest.of(dto.getPage() - 1, dto.getSize(), Sort.by("name").ascending());
        return bookRepository.findAllPagination(pageable);
    }

    private Book getByIdInternal(Long bookId) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        return optionalBook.orElseThrow(() -> new BusinessLogicException(ExceptionCode.BOOK_NOT_FOUND));
    }
}
