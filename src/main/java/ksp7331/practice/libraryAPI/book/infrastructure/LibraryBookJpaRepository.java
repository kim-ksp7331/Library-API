package ksp7331.practice.libraryAPI.book.infrastructure;

import ksp7331.practice.libraryAPI.book.infrastructure.entity.LibraryBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryBookJpaRepository extends JpaRepository<LibraryBook, Long>, LibraryBookCustomRepository {
}
