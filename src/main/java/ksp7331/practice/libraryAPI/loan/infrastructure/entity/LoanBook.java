package ksp7331.practice.libraryAPI.loan.infrastructure.entity;

import ksp7331.practice.libraryAPI.book.entity.LibraryBook;
import ksp7331.practice.libraryAPI.loan.domain.LoanState;
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
    @Column(nullable = false)
    private LoanState state = LoanState.LOANED;
    private LocalDateTime returnDate;
    @ManyToOne
    @JoinColumn(name = "LOAN_ID")
    private Loan loan;
    @ManyToOne
    @JoinColumn(name = "BOOK_ID")
    private LibraryBook libraryBook;

    public static LoanBook from(ksp7331.practice.libraryAPI.loan.domain.LoanBook domain, Loan loan) {
        LoanBook loanBook = new LoanBook();
        loanBook.id = domain.getId();
        loanBook.returnDate = domain.getReturnDate();
        loanBook.libraryBook = domain.getLibraryBook();
        if(domain.getState() != null) loanBook.state = domain.getState();
        loanBook.loan = loan;
        return loanBook;
    }

    public void update(ksp7331.practice.libraryAPI.loan.domain.LoanBook loanBook) {
        if(this.id != loanBook.getId()) return;
        // libraryBook.update();
        this.returnDate = loanBook.getReturnDate();
        this.state = loanBook.getState();
    }

    public ksp7331.practice.libraryAPI.loan.domain.LoanBook to() {
        return ksp7331.practice.libraryAPI.loan.domain.LoanBook.builder()
                .id(id)
                .returnDate(returnDate)
                .libraryBook(libraryBook)
                .state(state)
                .build();
    }
}
