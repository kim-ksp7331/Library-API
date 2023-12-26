package ksp7331.practice.libraryAPI.book.infrastructure;

import ksp7331.practice.libraryAPI.book.infrastructure.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BookCustomRepository {
    Optional<Book> findByIdFetchJoin(Long id);

    Page<Book> findAllPagination(Pageable pageable);
}
