package ksp7331.practice.libraryAPI.book.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ksp7331.practice.libraryAPI.book.entity.Book;
import ksp7331.practice.libraryAPI.book.entity.QBook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static ksp7331.practice.libraryAPI.book.entity.QBook.*;

@Repository
@RequiredArgsConstructor
public class BookCustomRepositoryImpl implements BookCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Optional<Book> findByIdFetchJoin(Long id) {
        return jpaQueryFactory.selectFrom(book)
                .where(book.id.eq(id))
                .innerJoin(book.libraryBooks)
                .innerJoin(book.libraryBooks.any().library)
                .fetchJoin()
                .stream().findAny();
    }
}
