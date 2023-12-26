package ksp7331.practice.libraryAPI.book.service;

import ksp7331.practice.libraryAPI.book.domain.Book;
import ksp7331.practice.libraryAPI.book.dto.BookCreate;
import ksp7331.practice.libraryAPI.book.dto.BookPageCreate;
import ksp7331.practice.libraryAPI.book.dto.LibraryBookCreate;
import ksp7331.practice.libraryAPI.book.service.port.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @InjectMocks
    private BookService bookService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private LibraryBookService libraryBookService;

    @DisplayName("신규 book 엔티티 생성")
    @Test
    void createNewBook() {
        // given
        long libraryId = 1L;
        BookCreate bookCreate = BookCreate.builder().build();
        bookCreate.setLibraryId(libraryId);

        long bookId = 3L;
        BDDMockito.given(bookRepository.create(Mockito.any(Book.class))).willReturn(Book.builder().id(bookId).build());

        // when
        Long result = bookService.createNewBook(bookCreate);

        // then
        assertThat(result).isEqualTo(bookId);
    }

    @DisplayName("기존에 생성된 book 다른 library에 등록")
    @Test
    void addBookToLibrary() {
        // given
        long bookId = 1L;
        long libraryId = 1L;
        LibraryBookCreate libraryBookCreate = LibraryBookCreate.builder()
                .bookId(bookId).libraryId(libraryId).build();
        BDDMockito.given(bookRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.ofNullable(Book.builder().build()));
        // when //then
        assertThatCode(() -> bookService.addBookToLibrary(libraryBookCreate)).doesNotThrowAnyException();
    }

    @DisplayName("id를 통해 book 엔티티 조회")
    @Test
    void findBook() {
        // given
        long bookId = 1L;

        Book book = Book.builder()
                .name("book1")
                .author("author1")
                .publisher("publisher1")
                .build();
        BDDMockito.given(bookRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.ofNullable(book));

        // when
        Book result = bookService.getById(bookId);

        // then
        assertThat(result).isEqualTo(book);
    }

    @Test
    @DisplayName("book 엔티티 페이지네이션 조회")
    void findBooks() {
        // given
        int page = 1;
        int size = 3;
        BookPageCreate param = BookPageCreate.builder().page(page).size(size).build();
        Page<Book> books = new PageImpl<>(List.of(Book.builder().build()));

        BDDMockito.given(bookRepository.findAllPagination(Mockito.any(Pageable.class))).willReturn(books);

        // when
        Page<Book> result = bookService.findAll(param);

        // then
        assertThat(result).isEqualTo(books);
    }
}