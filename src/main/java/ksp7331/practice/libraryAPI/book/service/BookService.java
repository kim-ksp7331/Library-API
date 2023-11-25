package ksp7331.practice.libraryAPI.book.service;

import ksp7331.practice.libraryAPI.book.dto.BookServiceDTO;
import ksp7331.practice.libraryAPI.book.entity.Book;
import ksp7331.practice.libraryAPI.book.mapper.BookMapper;
import ksp7331.practice.libraryAPI.book.repository.BookRepository;
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
    private final BookMapper bookMapper;

    public Long createNewBook(BookServiceDTO.CreateParam createParam) {
        Book book = bookMapper.serviceDTOToEntity(createParam);
        Book savedBook = bookRepository.save(book);
        libraryBookService.createLibraryBook(savedBook, createParam.getLibraryId());
        return savedBook.getId();
    }

    public void addBookToLibrary(BookServiceDTO.AddParam addParam) {
        Book book = findVerifiedBook(addParam.getBookId());
        libraryBookService.createLibraryBook(book, addParam.getLibraryId());
    }

    public BookServiceDTO.Result findBook(Long bookId) {
        Book book = findVerifiedBook(bookId);
        return bookMapper.entityToServiceDTO(book);
    }

    public Page<BookServiceDTO.Result> findBooks(BookServiceDTO.PageParam dto) {
        Pageable pageable = PageRequest.of(dto.getPage() - 1, dto.getSize(), Sort.by("name").ascending());
        return bookMapper.entitiesToServiceDTOs(bookRepository.findAllPagination(pageable));
    }

    private Book findVerifiedBook(Long bookId) {
        Optional<Book> optionalBook = bookRepository.findByIdFetchJoin(bookId);
        return optionalBook.orElseThrow(() -> new BusinessLogicException(ExceptionCode.BOOK_NOT_FOUND));
    }
}
