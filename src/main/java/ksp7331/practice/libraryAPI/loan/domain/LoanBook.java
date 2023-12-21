package ksp7331.practice.libraryAPI.loan.domain;

import ksp7331.practice.libraryAPI.book.entity.LibraryBook;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LoanBook {
    private Long id;
    private LoanState state = LoanState.LOANED;
    private LocalDateTime returnDate;
    private LibraryBook libraryBook;

    @Builder
    public LoanBook(Long id, LibraryBook libraryBook, LocalDateTime returnDate, LoanState state) {
        this.id = id;
        this.libraryBook = libraryBook;
        this.returnDate = returnDate;
        if(state != null) this.state = state;
    }

    public void returnBook() {
        state = LoanState.RETURN;
        returnDate = LocalDateTime.now();
        getLibraryBook().setState(LibraryBook.State.LOANABLE);
    }
}
