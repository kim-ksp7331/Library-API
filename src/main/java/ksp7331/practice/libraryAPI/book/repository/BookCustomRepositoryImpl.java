package ksp7331.practice.libraryAPI.book.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ksp7331.practice.libraryAPI.book.entity.Book;
import ksp7331.practice.libraryAPI.book.entity.QBook;
import ksp7331.practice.libraryAPI.book.entity.QLibraryBook;
import ksp7331.practice.libraryAPI.library.entity.QLibrary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static ksp7331.practice.libraryAPI.book.entity.QBook.*;
import static ksp7331.practice.libraryAPI.book.entity.QLibraryBook.*;
import static ksp7331.practice.libraryAPI.library.entity.QLibrary.*;

@Repository
@RequiredArgsConstructor
public class BookCustomRepositoryImpl implements BookCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Optional<Book> findByIdFetchJoin(Long id) {
        List<Book> books = jpaQueryFactory.selectFrom(book)
                .leftJoin(book.libraryBooks, libraryBook).fetchJoin()
                .leftJoin(libraryBook.library, library).fetchJoin()
                .where(book.id.eq(id))
                .fetch();
        return books.stream().findAny();
    }
}
