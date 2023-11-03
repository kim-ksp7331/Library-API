package ksp7331.practice.libraryAPI.loan.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import ksp7331.practice.libraryAPI.book.entity.QBook;
import ksp7331.practice.libraryAPI.book.entity.QLibraryBook;
import ksp7331.practice.libraryAPI.library.entity.QLibrary;
import ksp7331.practice.libraryAPI.loan.entity.Loan;
import ksp7331.practice.libraryAPI.loan.entity.QLoan;
import ksp7331.practice.libraryAPI.loan.entity.QLoanBook;
import ksp7331.practice.libraryAPI.member.entity.QLibraryMember;
import ksp7331.practice.libraryAPI.member.entity.QMember;
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
}
