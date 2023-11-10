package ksp7331.practice.libraryAPI.loan.entity;

import ksp7331.practice.libraryAPI.book.entity.Book;
import ksp7331.practice.libraryAPI.book.entity.LibraryBook;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class LoanTest {

    @Test
    void returnBooks() {
        // given
        long bookId = 1L;
        List<Long> bookIds = List.of(bookId);
        List<LibraryBook> libraryBooks = List.of(LibraryBook.builder().book(Book.builder().id(bookId).build()).build());
        Loan loan = Loan.builder().libraryBooks(libraryBooks).build();

        // when
        loan.returnBooks(bookIds);

        // then
        assertThat(loan.getLoanBooks().get(0).getState()).isEqualTo(LoanBook.State.RETURN);
    }
}