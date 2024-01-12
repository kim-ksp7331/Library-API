package ksp7331.practice.libraryAPI.library.infrastructure;

import ksp7331.practice.libraryAPI.library.infrastructure.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryJpaRepository extends JpaRepository<Library, Long> {
}
