package ksp7331.practice.libraryAPI.library.infrastructure.entity;

import ksp7331.practice.libraryAPI.library.domain.Library;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class LibraryEntityTest {

    @Test
    void from() {
        // given
        Library domain = Library.builder().id(1L).name("lib1").build();

        // when
        LibraryEntity library = LibraryEntity.from(domain);

        // then
        assertThat(library.getId()).isEqualTo(1L);
        assertThat(library.getName()).isEqualTo("lib1");
    }

    @Test
    void toDomain() {
        // given
        LibraryEntity entity = LibraryEntity.builder().id(1L).name("lib1").build();

        // when
        Library library = entity.toDomain();

        // then
        assertThat(library.getId()).isEqualTo(1L);
        assertThat(library.getName()).isEqualTo("lib1");
    }
}