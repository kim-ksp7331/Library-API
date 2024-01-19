package ksp7331.practice.libraryAPI.library.infrastructure;

import ksp7331.practice.libraryAPI.library.infrastructure.entity.LibraryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryJpaRepository extends JpaRepository<LibraryEntity, Long> {
}
