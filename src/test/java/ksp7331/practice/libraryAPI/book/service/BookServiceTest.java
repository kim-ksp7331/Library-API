package ksp7331.practice.libraryAPI.book.service;

import ksp7331.practice.libraryAPI.book.dto.BookServiceDTO;
import ksp7331.practice.libraryAPI.book.entity.Book;
import ksp7331.practice.libraryAPI.book.mapper.BookMapper;
import ksp7331.practice.libraryAPI.book.repository.BookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void createNewBook() {
        // given
        long libraryId = 1L;
        BookServiceDTO.CreateParam createParam = BookServiceDTO.CreateParam.builder().libraryId(libraryId).build();

        long bookId = 3L;
        BDDMockito.given(bookMapper.ServiceDTOToEntity(Mockito.any(BookServiceDTO.CreateParam.class)))
                .willReturn(Book.builder().build());
        BDDMockito.given(bookRepository.save(Mockito.any(Book.class))).willReturn(Book.builder().id(bookId).build());

        // when
        Long result = bookService.createNewBook(createParam);

        // then
        assertThat(result).isEqualTo(bookId);
    }

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
}