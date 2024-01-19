package ksp7331.practice.libraryAPI.loan.infrastructure.entity;

import ksp7331.practice.libraryAPI.book.infrastructure.entity.LibraryBookEntity;
import ksp7331.practice.libraryAPI.common.entity.BaseTimeEntity;
import ksp7331.practice.libraryAPI.loan.domain.LoanBook;
import ksp7331.practice.libraryAPI.loan.domain.LoanState;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "LOAN_BOOK")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoanBookEntity extends BaseTimeEntity {
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
    private LoanEntity loan;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "BOOK_ID")
    private LibraryBookEntity libraryBook;

    @Builder
    public LoanBookEntity(Long id, LoanState state, LocalDateTime returnDate, LoanEntity loan, LibraryBookEntity libraryBook) {
        this.id = id;
        if(state != null) this.state = state;
        this.returnDate = returnDate;
        this.loan = loan;
        this.libraryBook = libraryBook;
    }

    public static LoanBookEntity from(LoanBook domain, LoanEntity loan) {
        LoanBookEntity loanBook = new LoanBookEntity();
        loanBook.id = domain.getId();
        loanBook.returnDate = domain.getReturnDate();
        loanBook.libraryBook = LibraryBookEntity.from(domain.getLibraryBook());
        if(domain.getState() != null) loanBook.state = domain.getState();
        loanBook.loan = loan;
        return loanBook;
    }

    public void update(LoanBook loanBook) {
        if(loanBook == null || this.id != loanBook.getId()) return;
        libraryBook.update(loanBook.getLibraryBook());
        Optional.ofNullable(loanBook.getReturnDate()).ifPresent(returnDate -> this.returnDate = returnDate);
        Optional.ofNullable(loanBook.getState()).ifPresent(state -> this.state = state);
    }

    public LoanBook toDomain() {
        return LoanBook.builder()
                .id(id)
                .returnDate(returnDate)
                .libraryBook(libraryBook.toDomain())
                .state(state)
                .build();
    }
}
