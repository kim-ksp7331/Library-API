package ksp7331.practice.libraryAPI.loan.domain;

import ksp7331.practice.libraryAPI.book.entity.LibraryBook;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class LoanBookTest {

    @Test
    void returnBook() {
        // given
        LoanBook loanBook = LoanBook.builder().libraryBook(LibraryBook.builder().build()).build();

        // when
        loanBook.returnBook();

        // then
        assertThat(loanBook.getReturnDate()).isNotNull();
        assertThat(loanBook.getState()).isEqualTo(LoanState.RETURN);
        assertThat(loanBook.getLibraryBook().getState()).isEqualTo(LibraryBook.State.LOANABLE);
    }
}