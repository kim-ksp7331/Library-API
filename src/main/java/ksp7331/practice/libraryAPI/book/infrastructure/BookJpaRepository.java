package ksp7331.practice.libraryAPI.book.infrastructure;

import ksp7331.practice.libraryAPI.book.infrastructure.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookJpaRepository extends JpaRepository<BookEntity, Long>, BookCustomRepository {
}
