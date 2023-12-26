package ksp7331.practice.libraryAPI.book.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import ksp7331.practice.libraryAPI.book.domain.BookState;
import ksp7331.practice.libraryAPI.book.infrastructure.entity.LibraryBook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static ksp7331.practice.libraryAPI.book.entity.QLibraryBook.*;

@Repository
@RequiredArgsConstructor
public class LibraryBookCustomRepositoryImpl implements LibraryBookCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<LibraryBook> findByLibraryIdAndBookIds(Long libraryId, List<Long> bookIds) {
        return jpaQueryFactory.selectFrom(libraryBook)
                .innerJoin(libraryBook.book).fetchJoin()
                .where(libraryBook.library.id.eq(libraryId))
                .where(libraryBook.book.id.in(bookIds).and(libraryBook.state.eq(BookState.LOANABLE)))
                .fetch();
    }
}
