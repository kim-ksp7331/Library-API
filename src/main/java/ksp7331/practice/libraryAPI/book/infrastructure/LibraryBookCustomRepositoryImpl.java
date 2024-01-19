package ksp7331.practice.libraryAPI.book.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import ksp7331.practice.libraryAPI.book.domain.BookState;
import ksp7331.practice.libraryAPI.book.infrastructure.entity.LibraryBookEntity;
import ksp7331.practice.libraryAPI.book.infrastructure.entity.QLibraryBookEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static ksp7331.practice.libraryAPI.book.infrastructure.entity.QLibraryBookEntity.*;


@Repository
@RequiredArgsConstructor
public class LibraryBookCustomRepositoryImpl implements LibraryBookCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<LibraryBookEntity> findByLibraryIdAndBookIds(Long libraryId, List<Long> bookIds) {
        return jpaQueryFactory.selectFrom(libraryBookEntity)
                .innerJoin(libraryBookEntity.book).fetchJoin()
                .where(libraryBookEntity.library.id.eq(libraryId))
                .where(libraryBookEntity.book.id.in(bookIds).and(libraryBookEntity.state.eq(BookState.LOANABLE)))
                .fetch();
    }
}
