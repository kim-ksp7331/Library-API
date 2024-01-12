package ksp7331.practice.libraryAPI.member.domain;

import ksp7331.practice.libraryAPI.exception.BusinessLogicException;
import ksp7331.practice.libraryAPI.exception.ExceptionCode;

import ksp7331.practice.libraryAPI.library.domain.Library;
import ksp7331.practice.libraryAPI.loan.domain.Loan;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class LibraryMember {
    private Long id;
    private Member member;
    private Library library;
    private List<Phone> phones = new ArrayList<>();
    private Integer loanBooksCount = 0;
    @Setter
    private LocalDate loanAvailableDay = LocalDate.EPOCH;

    public void addLoanBooksCount(Integer count) {
        this.loanBooksCount += count;
    }

    public LibraryMember(Long id, Member member, Library library, List<Phone> phones) {
        this.id = id;
        setMember(member);
        this.library = library;
        this.phones = phones;
    }

    private List<Loan> loans = new ArrayList<>();

    @Builder
    public LibraryMember(Long id, Member member, Library library, List<Phone> phones, Integer loanBooksCount, LocalDate loanAvailableDay, List<Loan> loans) {
        this.id = id;
        setMember(member);
        this.library = library;
        Optional.ofNullable(phones).ifPresent(p -> this.phones = p);
        Optional.ofNullable(loanBooksCount).ifPresent(l -> this.loanBooksCount = l);
        Optional.ofNullable(loanAvailableDay).ifPresent(l -> this.loanAvailableDay = l);
        Optional.ofNullable(loans).ifPresent(l -> this.loans = l);
    }

    private LibraryMember(Member member, Library library, String phone) {
        setMember(member);
        this.library = library;
        addPhone(phone);
    }

    public void addLoan(Loan loan) {
        loans.add(loan);
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

    public static LibraryMember from(Member member, Library library, String phone) {
        return new LibraryMember(member, library, phone);
    }
    private void checkDuplicatedPhoneNumber(String phoneNumber) {
        if(phones.contains(phoneNumber)) throw new BusinessLogicException(ExceptionCode.PHONE_DUPLICATED);
    }
}
