package ksp7331.practice.libraryAPI.member.infrastructure.entity;

import ksp7331.practice.libraryAPI.common.entity.BaseTimeEntity;
import ksp7331.practice.libraryAPI.library.infrastructure.entity.LibraryEntity;
import ksp7331.practice.libraryAPI.loan.infrastructure.entity.LoanEntity;
import ksp7331.practice.libraryAPI.member.domain.LibraryMember;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Table(name = "LIBRARY_MEMBER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LibraryMemberEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private MemberEntity member;
    @ManyToOne
    @JoinColumn(name = "LIBRARY_ID")
    private LibraryEntity library;
    @OneToMany(mappedBy = "libraryMember", cascade = CascadeType.ALL)
    private List<PhoneEntity> phones = new ArrayList<>();
    @Column(nullable = false)
    private Integer loanBooksCount = 0;
    @Column(nullable = false)
    @Setter
    private LocalDate loanAvailableDay = LocalDate.EPOCH;


    @Builder
    public LibraryMemberEntity(Long id, MemberEntity member, LibraryEntity library, String phone) {
        this.id = id;
        setMember(member);
        this.library = library;
        addPhone(phone);
    }
    private void setMember(MemberEntity member) {
        Optional.ofNullable(member).ifPresent(m -> {
            this.member = m;
            m.addLibraryMember(this);
        });
    }

    public void addPhone(String phoneNumber) {
        Optional.ofNullable(phoneNumber).ifPresent(number -> {
            PhoneEntity phone = PhoneEntity.builder()
                    .number(number)
                    .libraryMember(this)
                    .build();
            phones.add(phone);
        });
    }


    @OneToMany(mappedBy = "libraryMember", cascade = CascadeType.REMOVE)
    private List<LoanEntity> loans = new ArrayList<>();

    public static LibraryMemberEntity from(LibraryMember domain) {
        LibraryMemberEntity libraryMember = new LibraryMemberEntity();
        libraryMember.id = domain.getId();
        libraryMember.member = MemberEntity.from(domain.getMember());
        libraryMember.loanBooksCount = domain.getLoanBooksCount();
        libraryMember.loanAvailableDay = domain.getLoanAvailableDay();
        libraryMember.library = LibraryEntity.from(domain.getLibrary());
        libraryMember.phones = domain.getPhones().stream().map(phone -> PhoneEntity.from(phone, libraryMember)).collect(Collectors.toList());
        return libraryMember;
    }

    public LibraryMember toDomainSub() {
        return LibraryMember.builder()
                .id(id)
                .member(member.toDomain())
                .library(library.toDomain())
                .loanBooksCount(loanBooksCount)
                .loanAvailableDay(loanAvailableDay)
                .phones(phones.stream().map(PhoneEntity::toDomain).collect(Collectors.toList()))
                .build();
    }
    public LibraryMember toDomain() {
        LibraryMember domain = toDomainSub();
        loans.forEach(l -> l.toDomainSub(domain));
        return domain;
    }
}
