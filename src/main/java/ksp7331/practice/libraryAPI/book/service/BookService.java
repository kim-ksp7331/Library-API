package ksp7331.practice.libraryAPI.book.service;

import ksp7331.practice.libraryAPI.book.dto.BookServiceDTO;
import ksp7331.practice.libraryAPI.book.entity.Book;
import ksp7331.practice.libraryAPI.book.mapper.BookMapper;
import ksp7331.practice.libraryAPI.book.repository.BookRepository;
import ksp7331.practice.libraryAPI.exception.BusinessLogicException;
import ksp7331.practice.libraryAPI.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final LibraryBookService libraryBookService;
    private final BookMapper bookMapper;

    public Long createNewBook(BookServiceDTO.CreateParam createParam) {
        Book book = bookMapper.ServiceDTOToEntity(createParam);
        Book savedBook = bookRepository.save(book);
        libraryBookService.createLibraryBook(savedBook, createParam.getLibraryId());
        return savedBook.getId();
    }

    public void addBookToLibrary(BookServiceDTO.AddParam addParam) {
        Book book = findVerifiedBook(addParam.getBookId());
        libraryBookService.createLibraryBook(book, addParam.getLibraryId());
    }

    private Book findVerifiedBook(Long bookId) {
        Optional<Book> optionalBook = bookRepository.findByIdFetchJoin(bookId);
        return optionalBook.orElseThrow(() -> new BusinessLogicException(ExceptionCode.BOOK_NOT_FOUND));
    }
}
