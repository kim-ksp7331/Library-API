package ksp7331.practice.libraryAPI.library.repository;

import ksp7331.practice.libraryAPI.config.DbTestConfig;
import ksp7331.practice.libraryAPI.library.entity.Library;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(DbTestConfig.class)
class LibraryRepositoryTest {
    @Autowired
    private LibraryRepository libraryRepository;
    @Test
    void saveTest() {
        // given
        String libraryName = "newLib";
        Library library = Library.builder()
                .name(libraryName)
                .build();
        // when
        Library savedLibrary = libraryRepository.save(library);

        // then
        assertThat(savedLibrary.getId()).isPositive();
    }

}