package ksp7331.practice.libraryAPI.book.infrastructure;

import ksp7331.practice.libraryAPI.book.infrastructure.entity.LibraryBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryBookJpaRepository extends JpaRepository<LibraryBookEntity, Long>, LibraryBookCustomRepository {
}
