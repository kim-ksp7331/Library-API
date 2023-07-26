package ksp7331.practice.libraryAPI.library.repository;

import ksp7331.practice.libraryAPI.QueryDslConfig;
import ksp7331.practice.libraryAPI.library.entity.Library;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QueryDslConfig.class)
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
        Assertions.assertThat(savedLibrary.getId()).isEqualTo(1L);
    }

}