package ksp7331.practice.libraryAPI.loan.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ksp7331.practice.libraryAPI.loan.entity.Loan;
import ksp7331.practice.libraryAPI.loan.entity.LoanBook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static ksp7331.practice.libraryAPI.book.entity.QBook.*;
import static ksp7331.practice.libraryAPI.book.entity.QLibraryBook.*;
import static ksp7331.practice.libraryAPI.library.entity.QLibrary.*;
import static ksp7331.practice.libraryAPI.loan.entity.QLoan.*;
import static ksp7331.practice.libraryAPI.loan.entity.QLoanBook.*;
import static ksp7331.practice.libraryAPI.member.entity.QLibraryMember.*;
import static ksp7331.practice.libraryAPI.member.entity.QMember.*;

@Repository
@RequiredArgsConstructor
public class LoanCustomRepositoryImpl implements LoanCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Loan> findByIdFetchJoin(Long id) {
        List<Loan> loans = joinForBook(jpaQueryFactory.selectFrom(loan)
                .where(loan.id.eq(id)))
                .fetch();
        return loans.stream().findAny();
    }

    @Override
    public List<Loan> findAllNotReturned(Long libraryMemberId) {
        List<Loan> loans = joinForBook(jpaQueryFactory.selectFrom(loan)
                .where(libraryMember.id.eq(libraryMemberId))
                .where(loanBook.state.eq(LoanBook.State.LOANED)))
                .distinct().fetch();
        return loans;
    }

    @Override
    public List<Loan> findByLibraryMemberIdAndMonth(Long libraryMemberId, int year, int month) {
        List<Loan> loans = joinForBook(jpaQueryFactory.selectFrom(loan)
                .where(libraryMember.id.eq(libraryMemberId))
                .where(loan.createdDate.year().eq(year).and(loan.createdDate.month().eq(month))))
                .distinct().fetch();
        return loans;
    }

    private JPAQuery<Loan> joinForBook(JPAQuery<Loan> jpaQueryBase) {
        return jpaQueryBase.innerJoin(loan.libraryMember, libraryMember).fetchJoin()
                .innerJoin(libraryMember.member, member).fetchJoin()
                .innerJoin(libraryMember.library, library).fetchJoin()
                .innerJoin(loan.loanBooks, loanBook).fetchJoin()
                .innerJoin(loanBook.libraryBook, libraryBook).fetchJoin()
                .innerJoin(libraryBook.book, book).fetchJoin();
    }
}
