package ksp7331.practice.libraryAPI.loan.domain;

import ksp7331.practice.libraryAPI.book.entity.Book;
import ksp7331.practice.libraryAPI.book.entity.LibraryBook;
import ksp7331.practice.libraryAPI.exception.BusinessLogicException;
import ksp7331.practice.libraryAPI.member.entity.LibraryMember;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class LoanTest {

    @Test
    void addBooks() {
        // given
        List<LibraryBook> libraryBooks = List.of(
                LibraryBook.builder().build()
        );
        Loan loan = Loan.builder().libraryMember(LibraryMember.builder().build()).build();

        // when
        loan.addBooks(libraryBooks);

        // then
        assertThat(loan.getLoanBooks().get(0).getLibraryBook()).isEqualTo(libraryBooks.get(0));
    }

    @Test
    void returnBooks() {
        // given
        List<Long> bookIds = List.of(3L, 4L);
        List<LoanBook> loanBooks = List.of(
                LoanBook.builder().libraryBook(LibraryBook.builder().book(Book.builder().id(1L).build()).build()).build(),
                LoanBook.builder().libraryBook(LibraryBook.builder().book(Book.builder().id(2L).build()).build()).build(),
                LoanBook.builder().libraryBook(LibraryBook.builder().book(Book.builder().id(3L).build()).build()).build(),
                LoanBook.builder().libraryBook(LibraryBook.builder().book(Book.builder().id(4L).build()).build()).build()
        );
        Loan loan = Loan.builder()
                .libraryMember(LibraryMember.builder().build())
                .createdDate(LocalDateTime.now())
                .loanBooks(loanBooks)
                .build();

        // when
        loan.returnBooks(bookIds);

        // then
        assertThat(loanBooks.get(0).getState()).isEqualTo(LoanState.LOANED);
        assertThat(loanBooks.get(1).getState()).isEqualTo(LoanState.LOANED);
        assertThat(loanBooks.get(2).getState()).isEqualTo(LoanState.RETURN);
        assertThat(loanBooks.get(3).getState()).isEqualTo(LoanState.RETURN);
    }

    @Test
    void checkOverDue() {
        // given
        Loan loan = Loan.builder()
                .libraryMember(LibraryMember.builder().build())
                .createdDate(LocalDateTime.now().minusDays(15))
                .build();

        // when
        // then
        assertThrows(BusinessLogicException.class, () -> loan.checkOverDue());
    }

    @Test
    void from() {
        // given
        LibraryMember libraryMember = LibraryMember.builder().build();
        List<LibraryBook> libraryBooks = List.of(LibraryBook.builder().build());

        // when
        Loan loan = Loan.from(libraryMember, libraryBooks);

        // then
        assertThat(loan.getLibraryMember()).isEqualTo(libraryMember);
        assertThat(loan.getLoanBooks().get(0).getLibraryBook()).isEqualTo(libraryBooks.get(0));
    }
}