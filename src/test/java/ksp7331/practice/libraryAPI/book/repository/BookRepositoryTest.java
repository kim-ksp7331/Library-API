package ksp7331.practice.libraryAPI.book.repository;

import ksp7331.practice.libraryAPI.QueryDslConfig;
import ksp7331.practice.libraryAPI.book.entity.Book;
import ksp7331.practice.libraryAPI.book.entity.LibraryBook;
import ksp7331.practice.libraryAPI.library.entity.Library;
import ksp7331.practice.libraryAPI.library.repository.LibraryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(QueryDslConfig.class)
class BookRepositoryTest {

    private BookRepository bookRepository;
    private LibraryRepository libraryRepository;
    private LibraryBookRepository libraryBookRepository;

    private String bookName = "Effective Java";
    private String author = "Joshua Bloch";
    private String libraryName = "new Lib";
    @Autowired
    public BookRepositoryTest(BookRepository bookRepository, LibraryRepository libraryRepository, LibraryBookRepository libraryBookRepository) {

        this.bookRepository = bookRepository;
        this.libraryRepository = libraryRepository;
        this.libraryBookRepository = libraryBookRepository;

        Book book = Book.builder().name(bookName).author(author).build();
        Book savedBook = bookRepository.save(book);
        Library library = Library.builder().name(libraryName).build();
        Library savedLibrary = libraryRepository.save(library);
        LibraryBook libraryBook = LibraryBook.builder().book(savedBook).library(savedLibrary).build();
        libraryBookRepository.save(libraryBook);
    }

    @Test
    void findByIdFetchJoin() {
        // given

        // when
        Optional<Book> optionalBook = bookRepository.findByIdFetchJoin(1L);

        // then
        assertThat(optionalBook.isPresent()).isTrue();
        Book result = optionalBook.get();
        assertThat(result.getName()).isEqualTo(bookName);
        assertThat(result.getAuthor()).isEqualTo(author);
        assertThat(result.getLibraryBooks()).anyMatch(lb -> lb.getLibrary().getName().equals(libraryName));
    }

}