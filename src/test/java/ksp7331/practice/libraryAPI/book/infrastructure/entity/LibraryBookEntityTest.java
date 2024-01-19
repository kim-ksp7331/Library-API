package ksp7331.practice.libraryAPI.book.infrastructure.entity;

import ksp7331.practice.libraryAPI.book.domain.Book;
import ksp7331.practice.libraryAPI.book.domain.BookState;
import ksp7331.practice.libraryAPI.book.domain.LibraryBook;
import ksp7331.practice.libraryAPI.library.domain.Library;
import ksp7331.practice.libraryAPI.library.infrastructure.entity.LibraryEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class LibraryBookEntityTest {

    @Test
    void from() {
        // given
        LibraryBook domain = LibraryBook.builder()
                .id(1L)
                .state(BookState.NOT_LOANABLE)
                .library(Library.builder().id(3L).build())
                .book(Book.builder()
                        .id(2L)
                        .name("book1")
                        .author("author1")
                        .publisher("publisher1")
                        .build())
                .build();

        // when
        LibraryBookEntity libraryBook = LibraryBookEntity.from(domain);

        // then
        assertThat(libraryBook.getId()).isEqualTo(1L);
        assertThat(libraryBook.getState()).isEqualTo(BookState.NOT_LOANABLE);
        assertThat(libraryBook.getBook().getId()).isEqualTo(2L);
        assertThat(libraryBook.getBook().getName()).isEqualTo("book1");
        assertThat(libraryBook.getBook().getAuthor()).isEqualTo("author1");
        assertThat(libraryBook.getBook().getPublisher()).isEqualTo("publisher1");
        assertThat(libraryBook.getLibrary().getId()).isEqualTo(3L);
    }

    @Test
    void toDomain() {
        // given
        BookEntity book = BookEntity.builder()
                .id(2L)
                .name("book1")
                .author("author1")
                .publisher("publisher1")
                .build();
        LibraryBookEntity libraryBook = LibraryBookEntity.builder().book(book).id(1L).state(BookState.NOT_LOANABLE)
                .library(LibraryEntity.builder().id(3L).build()).build();

        // when
        LibraryBook domain = libraryBook.toDomain();

        // then
        assertThat(domain.getId()).isEqualTo(1L);
        assertThat(domain.getState()).isEqualTo(BookState.NOT_LOANABLE);
        assertThat(domain.getBook().getId()).isEqualTo(2L);
        assertThat(domain.getBook().getName()).isEqualTo("book1");
        assertThat(domain.getBook().getAuthor()).isEqualTo("author1");
        assertThat(domain.getBook().getPublisher()).isEqualTo("publisher1");
        assertThat(domain.getLibrary().getId()).isEqualTo(3L);
    }

    @Test
    void update() {
        // given
        BookEntity book = BookEntity.builder()
                .id(2L)
                .name("book1")
                .author("author1")
                .publisher("publisher1")
                .build();
        LibraryBookEntity libraryBook = LibraryBookEntity.builder().book(book).id(1L).state(BookState.NOT_LOANABLE)
                .library(LibraryEntity.builder().id(3L).build()).build();

        LibraryBook updatedDomain = LibraryBook.builder()
                .state(BookState.LOANABLE)
                .build();


        // when
        libraryBook.update(updatedDomain);

        // then
        assertThat(libraryBook.getId()).isEqualTo(1L);
        assertThat(libraryBook.getState()).isEqualTo(BookState.LOANABLE);
        assertThat(libraryBook.getBook().getId()).isEqualTo(2L);
        assertThat(libraryBook.getBook().getName()).isEqualTo("book1");
        assertThat(libraryBook.getBook().getAuthor()).isEqualTo("author1");
        assertThat(libraryBook.getBook().getPublisher()).isEqualTo("publisher1");
        assertThat(libraryBook.getLibrary().getId()).isEqualTo(3L);
    }
}