package ksp7331.practice.libraryAPI.loan.entity;

import ksp7331.practice.libraryAPI.book.entity.LibraryBook;
import ksp7331.practice.libraryAPI.common.entity.BaseTimeEntity;
import ksp7331.practice.libraryAPI.exception.BusinessLogicException;
import ksp7331.practice.libraryAPI.exception.ExceptionCode;
import ksp7331.practice.libraryAPI.member.entity.LibraryMember;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Loan extends BaseTimeEntity {
    private static final int MAX_LOANABLE_BOOKS = 5;
    private static final int MAX_LOANABLE_DAYS = 14;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "LIBRARY_MEMBER_ID")
    private LibraryMember libraryMember;
    @OneToMany(mappedBy = "loan", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<LoanBook> loanBooks = new ArrayList<>();

    @Builder
    public Loan(Long id, LibraryMember libraryMember, List<LibraryBook> libraryBooks) {
        this.id = id;
        this.libraryMember = libraryMember;
        addBooks(libraryBooks);
    }

    public void addBooks(List<LibraryBook> libraryBooks) {
        if(libraryBooks == null) return;
        int count = libraryBooks.size();
        checkBookLoanable(count);
        libraryMember.addLoanBooksCount(count);
        libraryBooks.forEach(book -> {
            loanBooks.add(LoanBook.builder()
                    .libraryBook(book)
                    .loan(this)
                    .build());
            book.setState(LibraryBook.State.NOT_LOANABLE);
        });
    }

    public void returnBooks(List<Long> bookIds) {
        for (LoanBook loanBook : loanBooks) {
            if (bookIds.contains(loanBook.getLibraryBook().getBook().getId())) {
                loanBook.returnBook();
                libraryMember.addLoanBooksCount(-1);
                LocalDate returnDate = loanBook.getReturnDate().toLocalDate();
                int days = Period.between(getCreatedDate().toLocalDate(), returnDate).getDays();
                if (days > MAX_LOANABLE_DAYS) {
                    libraryMember.setLoanAvailableDay(returnDate.plusDays(days - MAX_LOANABLE_DAYS));
                }
            }
        }
    }

    private void checkBookLoanable(int count) {
        if (count + libraryMember.getLoanBooksCount() > MAX_LOANABLE_BOOKS) throw new BusinessLogicException(ExceptionCode.LOAN_EXCEEDED);
    }

    public void checkOverDue() {
        int days = Period.between(getCreatedDate().toLocalDate(), LocalDate.now()).getDays();
        if(days > MAX_LOANABLE_DAYS) throw new BusinessLogicException(ExceptionCode.LOAN_RESTRICTED);
    }

}
