package ksp7331.practice.libraryAPI.book.repository;

import ksp7331.practice.libraryAPI.QueryDslConfig;
import ksp7331.practice.libraryAPI.book.entity.Book;
import ksp7331.practice.libraryAPI.book.entity.LibraryBook;
import ksp7331.practice.libraryAPI.library.entity.Library;
import ksp7331.practice.libraryAPI.library.repository.LibraryRepository;
import org.assertj.core.api.ObjectAssert;
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

    @Autowired
    private BookRepository bookRepository;


    @Test
    void findByIdFetchJoin() {
        // given

        // when
        Optional<Book> optionalBook = bookRepository.findByIdFetchJoin(1L);

        // then
        assertThat(optionalBook.isPresent()).isTrue();
        Book result = optionalBook.get();
        assertThat(result.getName()).isEqualTo("Effective Java");
        assertThat(result.getAuthor()).isEqualTo("Joshua Bloch");
        assertLibrary(result, 0, "서울");
        assertLibrary(result, 1, "부산");
    }

    private void assertLibrary(Book result, int index, String libraryName) {
        assertThat(result.getLibraryBooks().get(index).getLibrary().getName()).isEqualTo(libraryName);
    }

}