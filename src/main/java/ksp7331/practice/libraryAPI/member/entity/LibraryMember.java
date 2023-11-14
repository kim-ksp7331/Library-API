package ksp7331.practice.libraryAPI.member.entity;

import ksp7331.practice.libraryAPI.common.entity.BaseTimeEntity;
import ksp7331.practice.libraryAPI.exception.BusinessLogicException;
import ksp7331.practice.libraryAPI.exception.ExceptionCode;
import ksp7331.practice.libraryAPI.library.entity.Library;
import ksp7331.practice.libraryAPI.loan.entity.Loan;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public void addLoanBooksCount(Integer count) {
        this.loanBooksCount += count;
    }

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
            checkDuplicatedPhoneNumber(number);
            Phone phone = Phone.builder()
                    .number(number)
                    .libraryMember(this)
                    .build();
            phones.add(phone);
        });
    }

    private void checkDuplicatedPhoneNumber(String phoneNumber) {
        if(phones.contains(phoneNumber)) throw new BusinessLogicException(ExceptionCode.PHONE_DUPLICATED);
    }
    @OneToMany(mappedBy = "libraryMember", cascade = CascadeType.REMOVE)
    private List<Loan> loans = new ArrayList<>();
}
