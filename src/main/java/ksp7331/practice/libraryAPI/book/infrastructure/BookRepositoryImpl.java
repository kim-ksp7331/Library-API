package ksp7331.practice.libraryAPI.book.infrastructure;

import ksp7331.practice.libraryAPI.book.domain.Book;
import ksp7331.practice.libraryAPI.book.infrastructure.entity.BookEntity;
import ksp7331.practice.libraryAPI.book.service.port.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {
    private final BookJpaRepository bookJpaRepository;
    @Override
    public Book create(Book book) {
        return bookJpaRepository.save(BookEntity.from(book)).toDomain();
    }

    @Override
    public Optional<Book> findById(Long id) {
        return bookJpaRepository.findByIdFetchJoin(id).map(BookEntity::toDomain);
    }

    @Override
    public Page<Book> findAllPagination(Pageable pageable) {
        return bookJpaRepository.findAllPagination(pageable).map(BookEntity::toDomain);
    }
}
