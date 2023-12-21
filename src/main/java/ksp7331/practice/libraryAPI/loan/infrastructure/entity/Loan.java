package ksp7331.practice.libraryAPI.loan.infrastructure.entity;

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
import java.util.stream.Collectors;

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

    public static Loan from(ksp7331.practice.libraryAPI.loan.domain.Loan domain) {
        Loan loan = new Loan();
        loan.id = domain.getId();
        loan.libraryMember = domain.getLibraryMember();
        loan.loanBooks = domain.getLoanBooks().stream().map(book -> LoanBook.from(book, loan)).collect(Collectors.toList());
        return loan;
    }

    public void update(ksp7331.practice.libraryAPI.loan.domain.Loan loan) {
        // libraryMember.update();
        loan.getLoanBooks().forEach(book -> {
            this.loanBooks.stream().filter(b -> b.getId() == book.getId()).forEach(b -> b.update(book));
        });
    }

    public ksp7331.practice.libraryAPI.loan.domain.Loan to() {
        return ksp7331.practice.libraryAPI.loan.domain.Loan.builder()
                .id(id)
                .createdDate(getCreatedDate())
                .libraryMember(libraryMember)
                .loanBooks(this.loanBooks.stream().map(book -> book.to()).collect(Collectors.toList()))
                .build();
    }
}