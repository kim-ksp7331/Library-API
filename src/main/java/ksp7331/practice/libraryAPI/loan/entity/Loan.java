package ksp7331.practice.libraryAPI.loan.entity;

import ksp7331.practice.libraryAPI.book.entity.LibraryBook;
import ksp7331.practice.libraryAPI.common.entity.BaseTimeEntity;
import ksp7331.practice.libraryAPI.exception.BusinessLogicException;
import ksp7331.practice.libraryAPI.exception.ExceptionCode;
import ksp7331.practice.libraryAPI.member.entity.LibraryMember;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Loan extends BaseTimeEntity {
    private static int MAX_LOANABLE_BOOKS = 5;
    private static int MAX_LOANABLE_DAYS = 14;
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
        if(libraryBooks != null) libraryBooks.forEach(this::addBook);
    }

    public void addBook(LibraryBook book) {
        if(book == null) return;
        checkBookLoanable();
        LoanBook loanBook = LoanBook.builder()
                .libraryBook(book)
                .loan(this)
                .build();
        loanBooks.add(loanBook);
    }

    private void checkBookLoanable() {
        if (loanBooks.size() >= MAX_LOANABLE_BOOKS) throw new BusinessLogicException(ExceptionCode.LOAN_EXCEEDED);
    }
}
