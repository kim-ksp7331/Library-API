package ksp7331.practice.libraryAPI.book.infrastructure;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ksp7331.practice.libraryAPI.book.infrastructure.entity.BookEntity;
import ksp7331.practice.libraryAPI.book.infrastructure.entity.QBookEntity;
import ksp7331.practice.libraryAPI.book.infrastructure.entity.QLibraryBookEntity;
import ksp7331.practice.libraryAPI.library.infrastructure.entity.QLibraryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static ksp7331.practice.libraryAPI.book.infrastructure.entity.QBookEntity.*;
import static ksp7331.practice.libraryAPI.book.infrastructure.entity.QLibraryBookEntity.*;
import static ksp7331.practice.libraryAPI.library.infrastructure.entity.QLibraryEntity.*;


@Repository
public class BookCustomRepositoryImpl extends QuerydslRepositorySupport implements BookCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    public BookCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(BookEntity.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Optional<BookEntity> findByIdFetchJoin(Long id) {
        List<BookEntity> books = jpaQueryFactory.selectFrom(bookEntity)
                .leftJoin(bookEntity.libraryBooks, libraryBookEntity).fetchJoin()
                .leftJoin(libraryBookEntity.library, libraryEntity).fetchJoin()
                .where(bookEntity.id.eq(id))
                .fetch();
        return books.stream().findAny();
    }

    @Override
    public Page<BookEntity> findAllPagination(Pageable pageable) {
        JPAQuery<BookEntity> query = jpaQueryFactory.selectFrom(bookEntity);
        Long count = jpaQueryFactory.select(bookEntity.count()).from(bookEntity).fetchOne();

        List<BookEntity> list = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(list, pageable, count);
    }
}
