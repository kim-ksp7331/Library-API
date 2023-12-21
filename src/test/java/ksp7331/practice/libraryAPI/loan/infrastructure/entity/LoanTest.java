package ksp7331.practice.libraryAPI.loan.infrastructure.entity;

import ksp7331.practice.libraryAPI.book.entity.Book;
import ksp7331.practice.libraryAPI.book.entity.LibraryBook;
import ksp7331.practice.libraryAPI.loan.domain.LoanBook;
import ksp7331.practice.libraryAPI.loan.domain.LoanState;
import ksp7331.practice.libraryAPI.member.entity.LibraryMember;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class LoanTest {

    @Test
    void from() {
        // given
        LibraryMember libraryMember = LibraryMember.builder().id(3L).build();
        LocalDateTime returnDate = LocalDateTime.now().minusDays(2);
        LibraryBook libraryBook = LibraryBook.builder().build();
        List<LoanBook> loanBooks = List.of(
                LoanBook.builder()
                        .id(2L)
                        .state(LoanState.RETURN)
                        .returnDate(returnDate)
                        .libraryBook(libraryBook)
                        .build()
        );
        ksp7331.practice.libraryAPI.loan.domain.Loan domain = ksp7331.practice.libraryAPI.loan.domain.Loan.builder()
                .id(1L)
                .libraryMember(libraryMember)
                .loanBooks(loanBooks)
                .build();

        // when
        Loan loan = Loan.from(domain);

        // then
        assertThat(loan.getLibraryMember().getId()).isEqualTo(libraryMember.getId());
        assertThat(loan.getLoanBooks().get(0).getId()).isEqualTo(2L);
        assertThat(loan.getLoanBooks().get(0).getState()).isEqualTo(LoanState.RETURN);
        assertThat(loan.getLoanBooks().get(0).getReturnDate()).isEqualTo(returnDate);
        assertThat(loan.getLoanBooks().get(0).getLibraryBook()).isEqualTo(libraryBook);
        assertThat(loan.getLoanBooks().get(0).getLoan()).isEqualTo(loan);
    }

    @Test
    void update() {
        // given
        LibraryMember libraryMember = LibraryMember.builder().id(3L).build();
        LibraryBook libraryBook = LibraryBook.builder().build();
        List<LoanBook> loanBooks = List.of(
                LoanBook.builder()
                        .id(1L)
                        .state(LoanState.LOANED)
                        .libraryBook(libraryBook)
                        .build(),
                LoanBook.builder()
                        .id(2L)
                        .state(LoanState.LOANED)
                        .libraryBook(libraryBook)
                        .build()
        );
        ksp7331.practice.libraryAPI.loan.domain.Loan domain = ksp7331.practice.libraryAPI.loan.domain.Loan.builder()
                .id(1L)
                .libraryMember(libraryMember)
                .loanBooks(loanBooks)
                .build();

        Loan loan = Loan.from(domain);


        LocalDateTime returnDate = LocalDateTime.now().minusDays(2);
        List<LoanBook> updatedLoanBooks = List.of(
                LoanBook.builder()
                        .id(2L)
                        .state(LoanState.RETURN)
                        .returnDate(returnDate)
                        .libraryBook(libraryBook)
                        .build()
        );

        ksp7331.practice.libraryAPI.loan.domain.Loan updatedDomain = ksp7331.practice.libraryAPI.loan.domain.Loan.builder()
                .id(1L)
                .libraryMember(libraryMember)
                .loanBooks(updatedLoanBooks)
                .build();


        // when
        loan.update(updatedDomain);


        // then
        assertThat(loan.getLoanBooks().get(0).getState()).isEqualTo(LoanState.LOANED);
        assertThat(loan.getLoanBooks().get(0).getReturnDate()).isNull();
        assertThat(loan.getLoanBooks().get(1).getState()).isEqualTo(LoanState.RETURN);
        assertThat(loan.getLoanBooks().get(1).getReturnDate()).isEqualTo(returnDate);
    }

    @Test
    void to() {
        // given
        LibraryMember libraryMember = LibraryMember.builder().id(3L).build();
        LocalDateTime returnDate = LocalDateTime.now().minusDays(2);
        LibraryBook libraryBook = LibraryBook.builder().book(Book.builder().id(1L).build()).build();
        List<LoanBook> loanBooks = List.of(
                LoanBook.builder()
                        .id(2L)
                        .state(LoanState.RETURN)
                        .returnDate(returnDate)
                        .libraryBook(libraryBook)
                        .build()
        );
        ksp7331.practice.libraryAPI.loan.domain.Loan domain = ksp7331.practice.libraryAPI.loan.domain.Loan.builder()
                .id(1L)
                .libraryMember(libraryMember)
                .loanBooks(loanBooks)
                .build();
        Loan loan = Loan.from(domain);

        // when
        ksp7331.practice.libraryAPI.loan.domain.Loan result = loan.to();

        // then
        assertThat(result.getId()).isEqualTo(domain.getId());
        assertThat(result.getLibraryMember().getId()).isEqualTo(libraryMember.getId());
        assertThat(result.getLoanBooks().get(0).getId()).isEqualTo(domain.getLoanBooks().get(0).getId());
        assertThat(result.getLoanBooks().get(0).getState()).isEqualTo(domain.getLoanBooks().get(0).getState());
        assertThat(result.getLoanBooks().get(0).getReturnDate()).isEqualTo(domain.getLoanBooks().get(0).getReturnDate());
        assertThat(result.getLoanBooks().get(0).getLibraryBook().getBook().getId())
                .isEqualTo(domain.getLoanBooks().get(0).getLibraryBook().getBook().getId());
    }
}