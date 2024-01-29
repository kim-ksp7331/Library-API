package ksp7331.practice.libraryAPI.loan.infrastructure.entity;

import ksp7331.practice.libraryAPI.common.entity.BaseTimeEntity;
import ksp7331.practice.libraryAPI.loan.domain.Loan;
import ksp7331.practice.libraryAPI.member.domain.LibraryMember;
import ksp7331.practice.libraryAPI.member.infrastructure.entity.LibraryMemberEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "LOAN")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoanEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "LIBRARY_MEMBER_ID")
    private LibraryMemberEntity libraryMember;
    @OneToMany(mappedBy = "loan", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
    private List<LoanBookEntity> loanBooks = new ArrayList<>();

    @Builder
    public LoanEntity(Long id, LibraryMemberEntity libraryMember, List<LoanBookEntity> loanBooks) {
        this.id = id;
        this.libraryMember = libraryMember;
        this.libraryMember.addLoan(this);
        this.loanBooks = loanBooks;
        loanBooks.forEach(loanBook -> loanBook.setLoan(this));
    }

    public static LoanEntity from(Loan domain) {
        LoanEntity loan = new LoanEntity();
        loan.id = domain.getId();
        loan.libraryMember = LibraryMemberEntity.from(domain.getLibraryMember());
        loan.loanBooks = domain.getLoanBooks().stream().map(book -> LoanBookEntity.from(book, loan)).collect(Collectors.toList());
        return loan;
    }

    public void update(Loan loan) {
        loan.getLoanBooks().forEach(book -> {
            this.loanBooks.stream().filter(b -> b.getId() == book.getId()).forEach(b -> b.update(book));
        });
    }

    public Loan toDomain() {
        return toDomainSub(libraryMember.toDomainSub());
    }
    public Loan toDomainSub(LibraryMember libraryMember) {
        return Loan.builder()
                .id(id)
                .createdDate(getCreatedDate())
                .libraryMember(libraryMember)
                .loanBooks(this.loanBooks.stream().map(book -> book.toDomain()).collect(Collectors.toList()))
                .build();
    }
}
