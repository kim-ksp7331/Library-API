package ksp7331.practice.libraryAPI.loan.domain;

import ksp7331.practice.libraryAPI.book.domain.BookState;
import ksp7331.practice.libraryAPI.book.domain.LibraryBook;
import ksp7331.practice.libraryAPI.exception.BusinessLogicException;
import ksp7331.practice.libraryAPI.exception.ExceptionCode;

import ksp7331.practice.libraryAPI.member.domain.LibraryMember;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Loan {
    private static final int MAX_LOANABLE_BOOKS = 5;
    private static final int MAX_LOANABLE_DAYS = 14;
    private Long id;
    private LibraryMember libraryMember;
    private List<LoanBook> loanBooks = new ArrayList<>();
    private LocalDateTime createdDate;

    @Builder
    public Loan(Long id, LibraryMember libraryMember, List<LoanBook> loanBooks, LocalDateTime createdDate) {
        this.id = id;
        this.libraryMember = libraryMember;
        this.libraryMember.addLoan(this);
        this.createdDate = createdDate;
        if(loanBooks != null) this.loanBooks = loanBooks;
    }

    public void addBooks(List<LibraryBook> libraryBooks) {
        if(libraryBooks == null) return;
        int count = libraryBooks.size();
        checkBookLoanable(count);
        libraryMember.addLoanBooksCount(count);
        libraryBooks.forEach(book -> {
            loanBooks.add(LoanBook.builder()
                    .libraryBook(book)
                    .build());
            book.setState(BookState.NOT_LOANABLE);
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

    public static Loan from(LibraryMember libraryMember, List<LibraryBook> libraryBooks) {
        Loan loan = Loan.builder().libraryMember(libraryMember).build();
        loan.addBooks(libraryBooks);
        return loan;
    }
}
