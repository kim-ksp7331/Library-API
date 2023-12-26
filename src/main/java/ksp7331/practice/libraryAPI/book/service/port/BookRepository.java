package ksp7331.practice.libraryAPI.book.service.port;

import ksp7331.practice.libraryAPI.book.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BookRepository {
    Book create(Book book);

    Optional<Book> findById(Long id);
    Page<Book> findAllPagination(Pageable pageable);

}
