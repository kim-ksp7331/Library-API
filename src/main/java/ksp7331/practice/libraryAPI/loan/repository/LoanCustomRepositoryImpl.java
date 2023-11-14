package ksp7331.practice.libraryAPI.loan.repository;

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
        List<Loan> loans = jpaQueryFactory.selectFrom(loan)
                .where(loan.id.eq(id))
                .innerJoin(loan.libraryMember, libraryMember).fetchJoin()
                .innerJoin(libraryMember.member, member).fetchJoin()
                .innerJoin(libraryMember.library, library).fetchJoin()
                .innerJoin(loan.loanBooks, loanBook).fetchJoin()
                .innerJoin(loanBook.libraryBook, libraryBook).fetchJoin()
                .innerJoin(libraryBook.book, book).fetchJoin()
                .fetch();
        return loans.stream().findAny();
    }

    @Override
    public List<Loan> findAllNotReturned(Long libraryMemberId) {
        List<Loan> loans = jpaQueryFactory.selectFrom(loan)
                .innerJoin(loan.libraryMember, libraryMember).where(libraryMember.id.eq(libraryMemberId)).fetchJoin()
                .innerJoin(libraryMember.member, member).fetchJoin()
                .innerJoin(libraryMember.library, library).fetchJoin()
                .innerJoin(loan.loanBooks, loanBook).where(loanBook.state.eq(LoanBook.State.LOANED)).fetchJoin()
                .innerJoin(loanBook.libraryBook, libraryBook).fetchJoin()
                .innerJoin(libraryBook.book, book).fetchJoin()
                .distinct().fetch();
        return loans;
    }
}
