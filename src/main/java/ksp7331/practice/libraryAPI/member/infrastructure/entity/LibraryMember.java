package ksp7331.practice.libraryAPI.member.infrastructure.entity;

import ksp7331.practice.libraryAPI.common.entity.BaseTimeEntity;
import ksp7331.practice.libraryAPI.library.infrastructure.entity.Library;
import ksp7331.practice.libraryAPI.loan.infrastructure.entity.Loan;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LibraryMember extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "LIBRARY_ID")
    private Library library;
    @OneToMany(mappedBy = "libraryMember", cascade = CascadeType.ALL)
    private List<Phone> phones = new ArrayList<>();
    @Column(nullable = false)
    private Integer loanBooksCount = 0;
    @Column(nullable = false)
    @Setter
    private LocalDate loanAvailableDay = LocalDate.EPOCH;


    @Builder
    public LibraryMember(Long id, Member member, Library library, String phone) {
        this.id = id;
        setMember(member);
        this.library = library;
        addPhone(phone);
    }
    private void setMember(Member member) {
        Optional.ofNullable(member).ifPresent(m -> {
            this.member = m;
            m.addLibraryMember(this);
        });
    }

    public void addPhone(String phoneNumber) {
        Optional.ofNullable(phoneNumber).ifPresent(number -> {
            Phone phone = Phone.builder()
                    .number(number)
                    .libraryMember(this)
                    .build();
            phones.add(phone);
        });
    }


    @OneToMany(mappedBy = "libraryMember", cascade = CascadeType.REMOVE)
    private List<Loan> loans = new ArrayList<>();

    public static LibraryMember from(ksp7331.practice.libraryAPI.member.domain.LibraryMember domain) {
        LibraryMember libraryMember = new LibraryMember();
        libraryMember.id = domain.getId();
        libraryMember.member = Member.from(domain.getMember());
        libraryMember.loanBooksCount = domain.getLoanBooksCount();
        libraryMember.loanAvailableDay = domain.getLoanAvailableDay();
        libraryMember.library = Library.from(domain.getLibrary());
        libraryMember.phones = domain.getPhones().stream().map(phone -> Phone.from(phone, libraryMember)).collect(Collectors.toList());
        return libraryMember;
    }

    public ksp7331.practice.libraryAPI.member.domain.LibraryMember toDomainSub() {
        return ksp7331.practice.libraryAPI.member.domain.LibraryMember.builder()
                .id(id)
                .member(member.toDomain())
                .library(library.toDomain())
                .loanBooksCount(loanBooksCount)
                .loanAvailableDay(loanAvailableDay)
                .phones(phones.stream().map(Phone::toDomain).collect(Collectors.toList()))
                .build();
    }
    public ksp7331.practice.libraryAPI.member.domain.LibraryMember toDomain() {
        ksp7331.practice.libraryAPI.member.domain.LibraryMember domain = toDomainSub();
        loans.forEach(l -> l.toDomainSub(domain));
        return domain;
    }
}
