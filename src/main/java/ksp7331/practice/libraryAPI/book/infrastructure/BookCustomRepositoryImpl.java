package ksp7331.practice.libraryAPI.book.infrastructure;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ksp7331.practice.libraryAPI.book.infrastructure.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static ksp7331.practice.libraryAPI.book.entity.QBook.*;
import static ksp7331.practice.libraryAPI.book.entity.QLibraryBook.*;
import static ksp7331.practice.libraryAPI.library.entity.QLibrary.*;

@Repository
public class BookCustomRepositoryImpl extends QuerydslRepositorySupport implements BookCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    public BookCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Book.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Optional<Book> findByIdFetchJoin(Long id) {
        List<Book> books = jpaQueryFactory.selectFrom(book)
                .leftJoin(book.libraryBooks, libraryBook).fetchJoin()
                .leftJoin(libraryBook.library, library).fetchJoin()
                .where(book.id.eq(id))
                .fetch();
        return books.stream().findAny();
    }

    @Override
    public Page<Book> findAllPagination(Pageable pageable) {
        JPAQuery<Book> query = jpaQueryFactory.selectFrom(book);
        Long count = jpaQueryFactory.select(book.count()).from(book).fetchOne();

        List<Book> list = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(list, pageable, count);
    }
}
