package ksp7331.practice.libraryAPI.book.infrastructure.entity;

import ksp7331.practice.libraryAPI.book.domain.Book;
import ksp7331.practice.libraryAPI.library.infrastructure.entity.LibraryEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class BookEntityTest {

    @Test
    void from() {
        // given
        Book domain = Book.builder()
                .id(1L)
                .name("book1")
                .author("author1")
                .publisher("publisher1")
                .build();

        // when
        BookEntity result = BookEntity.from(domain);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("book1");
        assertThat(result.getAuthor()).isEqualTo("author1");
        assertThat(result.getPublisher()).isEqualTo("publisher1");
    }

    @Test
    void toDomain() {
        // given
        BookEntity book = BookEntity.builder()
                .id(1L)
                .name("book1")
                .author("author1")
                .publisher("publisher1")
                .build();
        LibraryBookEntity libraryBook = LibraryBookEntity.builder().id(1L).book(book).library(LibraryEntity.builder().build()).build();
        book.addLibraryBook(libraryBook);

        // when
        Book domain = book.toDomain();

        // then
        assertThat(domain.getId()).isEqualTo(1L);
        assertThat(domain.getName()).isEqualTo("book1");
        assertThat(domain.getAuthor()).isEqualTo("author1");
        assertThat(domain.getPublisher()).isEqualTo("publisher1");
        assertThat(domain.getLibraryBooks()).anyMatch(b -> b.getId().equals(1L));

    }
}