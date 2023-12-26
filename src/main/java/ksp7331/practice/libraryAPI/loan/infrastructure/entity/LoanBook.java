package ksp7331.practice.libraryAPI.loan.infrastructure.entity;

import ksp7331.practice.libraryAPI.book.infrastructure.entity.LibraryBook;
import ksp7331.practice.libraryAPI.common.entity.BaseTimeEntity;
import ksp7331.practice.libraryAPI.loan.domain.LoanState;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoanBook extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanState state = LoanState.LOANED;
    private LocalDateTime returnDate;
    @ManyToOne
    @JoinColumn(name = "LOAN_ID")
    @Setter(AccessLevel.PACKAGE)
    private Loan loan;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "BOOK_ID")
    private LibraryBook libraryBook;

    @Builder
    public LoanBook(Long id, LoanState state, LocalDateTime returnDate, Loan loan, LibraryBook libraryBook) {
        this.id = id;
        if(state != null) this.state = state;
        this.returnDate = returnDate;
        this.loan = loan;
        this.libraryBook = libraryBook;
    }

    public static LoanBook from(ksp7331.practice.libraryAPI.loan.domain.LoanBook domain, Loan loan) {
        LoanBook loanBook = new LoanBook();
        loanBook.id = domain.getId();
        loanBook.returnDate = domain.getReturnDate();
        loanBook.libraryBook = LibraryBook.from(domain.getLibraryBook());
        if(domain.getState() != null) loanBook.state = domain.getState();
        loanBook.loan = loan;
        return loanBook;
    }

    public void update(ksp7331.practice.libraryAPI.loan.domain.LoanBook loanBook) {
        if(loanBook == null || this.id != loanBook.getId()) return;
        libraryBook.update(loanBook.getLibraryBook());
        Optional.ofNullable(loanBook.getReturnDate()).ifPresent(returnDate -> this.returnDate = returnDate);
        Optional.ofNullable(loanBook.getState()).ifPresent(state -> this.state = state);
    }

    public ksp7331.practice.libraryAPI.loan.domain.LoanBook toDomain() {
        return ksp7331.practice.libraryAPI.loan.domain.LoanBook.builder()
                .id(id)
                .returnDate(returnDate)
                .libraryBook(libraryBook.toDomain())
                .state(state)
                .build();
    }
}
