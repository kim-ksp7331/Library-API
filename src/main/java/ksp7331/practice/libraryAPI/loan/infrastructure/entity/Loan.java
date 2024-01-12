package ksp7331.practice.libraryAPI.loan.infrastructure.entity;

import ksp7331.practice.libraryAPI.common.entity.BaseTimeEntity;
import ksp7331.practice.libraryAPI.member.infrastructure.entity.LibraryMember;
import lombok.*;

import javax.persistence.*;
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
    @OneToMany(mappedBy = "loan", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
    private List<LoanBook> loanBooks = new ArrayList<>();

    @Builder
    public Loan(Long id, LibraryMember libraryMember, List<LoanBook> loanBooks) {
        this.id = id;
        this.libraryMember = libraryMember;
        this.loanBooks = loanBooks;
        loanBooks.forEach(loanBook -> loanBook.setLoan(this));
    }

    public static Loan from(ksp7331.practice.libraryAPI.loan.domain.Loan domain) {
        Loan loan = new Loan();
        loan.id = domain.getId();
        loan.libraryMember = LibraryMember.from(domain.getLibraryMember());
        loan.loanBooks = domain.getLoanBooks().stream().map(book -> LoanBook.from(book, loan)).collect(Collectors.toList());
        return loan;
    }

    public void update(ksp7331.practice.libraryAPI.loan.domain.Loan loan) {
        // libraryMember.update();
        loan.getLoanBooks().forEach(book -> {
            this.loanBooks.stream().filter(b -> b.getId() == book.getId()).forEach(b -> b.update(book));
        });
    }

    public ksp7331.practice.libraryAPI.loan.domain.Loan toDomain() {
        return toDomainSub(libraryMember.toDomainSub());
    }
    public ksp7331.practice.libraryAPI.loan.domain.Loan toDomainSub(ksp7331.practice.libraryAPI.member.domain.LibraryMember libraryMember) {
        return ksp7331.practice.libraryAPI.loan.domain.Loan.builder()
                .id(id)
                .createdDate(getCreatedDate())
                .libraryMember(libraryMember)
                .loanBooks(this.loanBooks.stream().map(book -> book.toDomain()).collect(Collectors.toList()))
                .build();
    }
}
