package ksp7331.practice.libraryAPI.book.service;

import ksp7331.practice.libraryAPI.book.dto.BookServiceDTO;
import ksp7331.practice.libraryAPI.book.entity.Book;
import ksp7331.practice.libraryAPI.book.mapper.BookMapper;
import ksp7331.practice.libraryAPI.book.repository.BookRepository;
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
    @Mock
    private BookMapper bookMapper;

    @DisplayName("신규 book 엔티티 생성")
    @Test
    void createNewBook() {
        // given
        long libraryId = 1L;
        BookServiceDTO.CreateParam createParam = BookServiceDTO.CreateParam.builder().libraryId(libraryId).build();

        long bookId = 3L;
        BDDMockito.given(bookMapper.serviceDTOToEntity(Mockito.any(BookServiceDTO.CreateParam.class)))
                .willReturn(Book.builder().build());
        BDDMockito.given(bookRepository.save(Mockito.any(Book.class))).willReturn(Book.builder().id(bookId).build());

        // when
        Long result = bookService.createNewBook(createParam);

        // then
        assertThat(result).isEqualTo(bookId);
    }

    @DisplayName("기존에 생성된 book 다른 library에 등록")
    @Test
    void addBookToLibrary() {
        // given
        long bookId = 1L;
        long libraryId = 1L;
        BookServiceDTO.AddParam addParam = BookServiceDTO.AddParam.builder()
                .bookId(bookId).libraryId(libraryId).build();
        BDDMockito.given(bookRepository.findByIdFetchJoin(Mockito.anyLong()))
                .willReturn(Optional.ofNullable(Book.builder().build()));
        // when //then
        assertThatCode(() -> bookService.addBookToLibrary(addParam)).doesNotThrowAnyException();
    }

    @DisplayName("id를 통해 book 엔티티 조회")
    @Test
    void findBook() {
        // given
        long bookId = 1L;
        BookServiceDTO.Result book = BookServiceDTO.Result.builder()
                .name("book1")
                .author("author1")
                .build();

        BDDMockito.given(bookRepository.findByIdFetchJoin(Mockito.anyLong()))
                .willReturn(Optional.ofNullable(Book.builder().build()));
        BDDMockito.given(bookMapper.entityToServiceDTO(Mockito.any(Book.class))).willReturn(book);

        // when
        BookServiceDTO.Result result = bookService.findBook(bookId);

        // then
        assertThat(result).isEqualTo(book);
    }

    @Test
    @DisplayName("book 엔티티 페이지네이션 조회")
    void findBooks() {
        // given
        int page = 1;
        int size = 3;
        BookServiceDTO.PageParam param = BookServiceDTO.PageParam.builder().page(page).size(size).build();
        Page<Book> books = new PageImpl<>(List.of(Book.builder().build()));
        Page<BookServiceDTO.Result> pageResult = new PageImpl<>(List.of(BookServiceDTO.Result.builder().build()));

        BDDMockito.given(bookRepository.findAllPagination(Mockito.any(Pageable.class))).willReturn(books);
        BDDMockito.given(bookMapper.entitiesToServiceDTOs(books)).willReturn(pageResult);

        // when
        Page<BookServiceDTO.Result> result = bookService.findBooks(param);

        // then
        assertThat(result).isEqualTo(pageResult);
    }
}