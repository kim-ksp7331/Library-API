package ksp7331.practice.libraryAPI.book.repository;

import ksp7331.practice.libraryAPI.book.entity.LibraryBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryBookRepository extends JpaRepository<LibraryBook, Long>, LibraryBookCustomRepository {
}
