package ksp7331.practice.libraryAPI.loan.infrastructure;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ksp7331.practice.libraryAPI.loan.domain.LoanState;
import ksp7331.practice.libraryAPI.loan.infrastructure.entity.LoanEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static ksp7331.practice.libraryAPI.book.infrastructure.entity.QBookEntity.*;
import static ksp7331.practice.libraryAPI.book.infrastructure.entity.QLibraryBookEntity.*;
import static ksp7331.practice.libraryAPI.library.infrastructure.entity.QLibraryEntity.*;
import static ksp7331.practice.libraryAPI.loan.infrastructure.entity.QLoanBookEntity.*;
import static ksp7331.practice.libraryAPI.loan.infrastructure.entity.QLoanEntity.*;
import static ksp7331.practice.libraryAPI.member.infrastructure.entity.QLibraryMemberEntity.*;
import static ksp7331.practice.libraryAPI.member.infrastructure.entity.QMemberEntity.*;


@Repository
@RequiredArgsConstructor
public class LoanCustomRepositoryImpl implements LoanCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<LoanEntity> findByIdFetchJoin(Long id) {
        List<LoanEntity> loans = joinForBook(jpaQueryFactory.selectFrom(loanEntity)
                .where(loanEntity.id.eq(id)))
                .fetch();
        return loans.stream().findAny();
    }

    @Override
    public List<LoanEntity> findAllNotReturned(Long libraryMemberId) {
        List<LoanEntity> loans = joinForBook(jpaQueryFactory.selectFrom(loanEntity)
                .where(libraryMemberEntity.id.eq(libraryMemberId))
                .where(loanBookEntity.state.eq(LoanState.LOANED)))
                .distinct().fetch();
        return loans;
    }

    @Override
    public List<LoanEntity> findByLibraryMemberIdAndMonth(Long libraryMemberId, int year, int month) {
        List<LoanEntity> loans = joinForBook(jpaQueryFactory.selectFrom(loanEntity)
                .where(libraryMemberEntity.id.eq(libraryMemberId))
                .where(loanEntity.createdDate.year().eq(year).and(loanEntity.createdDate.month().eq(month))))
                .distinct().fetch();
        return loans;
    }

    private JPAQuery<LoanEntity> joinForBook(JPAQuery<LoanEntity> jpaQueryBase) {
        return jpaQueryBase.innerJoin(loanEntity.libraryMember, libraryMemberEntity).fetchJoin()
                .innerJoin(libraryMemberEntity.member, memberEntity).fetchJoin()
                .innerJoin(libraryMemberEntity.library, libraryEntity).fetchJoin()
                .innerJoin(loanEntity.loanBooks, loanBookEntity).fetchJoin()
                .innerJoin(loanBookEntity.libraryBook, libraryBookEntity).fetchJoin()
                .innerJoin(libraryBookEntity.book, bookEntity).fetchJoin();
    }
}
