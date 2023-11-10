package ksp7331.practice.libraryAPI.loan.entity;

import ksp7331.practice.libraryAPI.book.entity.LibraryBook;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoanBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private State state = State.BOOK;
    private LocalDateTime returnDate;
    @ManyToOne
    @JoinColumn(name = "LOAN_ID")
    private Loan loan;
    @ManyToOne
    @JoinColumn(name = "BOOK_ID")
    private LibraryBook libraryBook;

    @Builder
    public LoanBook(Loan loan, LibraryBook libraryBook) {
        this.loan = loan;
        this.libraryBook = libraryBook;
    }

    public void returnBook() {
        state = State.RETURN;
        returnDate = LocalDateTime.now();
    }

    public enum State {
        BOOK, RETURN
    }
}
