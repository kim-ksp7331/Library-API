package ksp7331.practice.libraryAPI.library.repository;

import ksp7331.practice.libraryAPI.library.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library, Long> {
}
