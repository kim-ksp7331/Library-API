package ksp7331.practice.libraryAPI.loan.infrastructure.entity;

import ksp7331.practice.libraryAPI.book.domain.BookState;
import ksp7331.practice.libraryAPI.book.infrastructure.entity.Book;
import ksp7331.practice.libraryAPI.book.infrastructure.entity.LibraryBook;
import ksp7331.practice.libraryAPI.loan.domain.LoanState;
import ksp7331.practice.libraryAPI.member.entity.LibraryMember;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class LoanTest {

    @Test
    void from() {
        // given
        LibraryMember libraryMember = LibraryMember.builder().id(3L).build();
        LocalDateTime returnDate = LocalDateTime.now().minusDays(2);
        ksp7331.practice.libraryAPI.book.domain.LibraryBook libraryBook = ksp7331.practice.libraryAPI.book.domain.LibraryBook.builder()
                .id(1L).state(BookState.LOANABLE).book(ksp7331.practice.libraryAPI.book.domain.Book.builder().id(1L).build()).build();
        List<ksp7331.practice.libraryAPI.loan.domain.LoanBook> loanBooks = List.of(
                ksp7331.practice.libraryAPI.loan.domain.LoanBook.builder()
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
        assertThat(loan.getLoanBooks().get(0).getLibraryBook().getId()).isEqualTo(libraryBook.getId());
        assertThat(loan.getLoanBooks().get(0).getLoan()).isEqualTo(loan);
    }

    @Test
    void update() {
        // given
        LibraryMember libraryMember = LibraryMember.builder().id(3L).build();
        ksp7331.practice.libraryAPI.book.domain.LibraryBook libraryBook = ksp7331.practice.libraryAPI.book.domain.LibraryBook
                .builder().id(1L).state(BookState.NOT_LOANABLE).book(ksp7331.practice.libraryAPI.book.domain.Book.builder().id(1L).build()).build();
        List<LoanBook> loanBooks = List.of(
                LoanBook.builder()
                        .id(1L)
                        .state(LoanState.LOANED)
                        .libraryBook(LibraryBook.from(libraryBook))
                        .build(),
                LoanBook.builder()
                        .id(2L)
                        .state(LoanState.LOANED)
                        .libraryBook(LibraryBook.from(libraryBook))
                        .build()
        );


        Loan loan = Loan.builder().id(1L).libraryMember(libraryMember).loanBooks(loanBooks).build();


        LocalDateTime returnDate = LocalDateTime.now().minusDays(2);
        List<ksp7331.practice.libraryAPI.loan.domain.LoanBook> updatedLoanBooks = List.of(
                ksp7331.practice.libraryAPI.loan.domain.LoanBook.builder()
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
    void toDomain() {
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

        Loan loan = Loan.builder().id(2L).libraryMember(libraryMember).loanBooks(loanBooks).build();

        // when
        ksp7331.practice.libraryAPI.loan.domain.Loan result = loan.toDomain();

        // then
        assertThat(result.getId()).isEqualTo(loan.getId());
        assertThat(result.getLibraryMember().getId()).isEqualTo(libraryMember.getId());
        assertThat(result.getLoanBooks().get(0).getId()).isEqualTo(loan.getLoanBooks().get(0).getId());
        assertThat(result.getLoanBooks().get(0).getState()).isEqualTo(loan.getLoanBooks().get(0).getState());
        assertThat(result.getLoanBooks().get(0).getReturnDate()).isEqualTo(loan.getLoanBooks().get(0).getReturnDate());
        assertThat(result.getLoanBooks().get(0).getLibraryBook().getBook().getId())
                .isEqualTo(loan.getLoanBooks().get(0).getLibraryBook().getBook().getId());
    }
}