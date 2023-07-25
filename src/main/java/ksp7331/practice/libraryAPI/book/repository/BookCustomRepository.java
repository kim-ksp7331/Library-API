package ksp7331.practice.libraryAPI.book.repository;

import ksp7331.practice.libraryAPI.book.entity.Book;

import java.util.Optional;

public interface BookCustomRepository {
    Optional<Book> findByIdFetchJoin(Long id);
}
