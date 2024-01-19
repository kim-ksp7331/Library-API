package ksp7331.practice.libraryAPI.book.infrastructure;

import ksp7331.practice.libraryAPI.book.infrastructure.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BookCustomRepository {
    Optional<BookEntity> findByIdFetchJoin(Long id);

    Page<BookEntity> findAllPagination(Pageable pageable);
}
