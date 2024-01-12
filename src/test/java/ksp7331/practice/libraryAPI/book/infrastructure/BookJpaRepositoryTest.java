package ksp7331.practice.libraryAPI.book.infrastructure;

import ksp7331.practice.libraryAPI.book.infrastructure.entity.Book;
import ksp7331.practice.libraryAPI.config.DbTestConfig;
import ksp7331.practice.libraryAPI.config.DbTestInitializer;
import ksp7331.practice.libraryAPI.library.infrastructure.entity.Library;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(DbTestConfig.class)
class BookJpaRepositoryTest {

    @Autowired
    private BookJpaRepository bookRepobookJpaRepository;
    @Autowired
    private DbTestInitializer dbTestInitializer;

    @Test
    void findByIdFetchJoin() {
        // given
        long bookId = 1L;
        Book expected = dbTestInitializer.getBooks().get(0);
        List<Library> libraries = dbTestInitializer.getLibraries();

        // when
        Optional<Book> optionalBook = bookRepobookJpaRepository.findByIdFetchJoin(bookId);

        // then
        assertThat(optionalBook.isPresent()).isTrue();
        Book result = optionalBook.get();
        assertThat(result.getName()).isEqualTo(expected.getName());
        assertThat(result.getAuthor()).isEqualTo(expected.getAuthor());
        assertThat(result.getPublisher()).isEqualTo(expected.getPublisher());
        for (int i = 0; i < 2; i++) {
            assertLibrary(result, i, libraries.get(i).getName());
        }
    }

    private void assertLibrary(Book result, int index, String libraryName) {
        assertThat(result.getLibraryBooks().get(index).getLibrary().getName()).isEqualTo(libraryName);
    }

    @Test
    void findAllPagination() {
        // given
        int size = 4;
        Pageable pageable = PageRequest.of(0, size, Sort.by("name").ascending());
        String[] bookNames = dbTestInitializer.getBooks()
                .stream().map(b -> b.getName()).sorted().limit(size).toArray(String[]::new);

        // when
        Page<Book> bookPage = bookRepobookJpaRepository.findAllPagination(pageable);

        // then
        assertThat(bookPage).hasSize(size);
        assertThat(bookPage).extracting(Book::getName).containsExactly(bookNames);
    }
}