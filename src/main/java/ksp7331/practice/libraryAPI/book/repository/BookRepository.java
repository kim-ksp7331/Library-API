package ksp7331.practice.libraryAPI.book.repository;

import ksp7331.practice.libraryAPI.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long>, BookCustomRepository {
}
