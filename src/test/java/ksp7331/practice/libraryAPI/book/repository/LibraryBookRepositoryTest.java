package ksp7331.practice.libraryAPI.book.repository;

import ksp7331.practice.libraryAPI.book.entity.LibraryBook;
import ksp7331.practice.libraryAPI.config.DbTestConfig;
import ksp7331.practice.libraryAPI.config.DbTestInitializer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@DataJpaTest
@Import(DbTestConfig.class)
public class LibraryBookRepositoryTest {
    @Autowired
    private LibraryBookRepository libraryBookRepository;
    @Autowired
    private DbTestInitializer dbTestInitializer;
    @Test
    void findByLibraryIdAndBookIds() {
        // given
        Long libraryId = 2L;
        List<Long> bookIds = List.of(1L, 2L, 3L, 5L);

        // when
        List<LibraryBook> libraryBooks = libraryBookRepository.findByLibraryIdAndBookIds(libraryId, bookIds);

        // then
        Assertions.assertThat(libraryBooks).hasSize(1);
    }
}
