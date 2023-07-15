package ksp7331.practice.libraryAPI.member.entity;

import ksp7331.practice.libraryAPI.common.entity.BaseTimeEntity;
import ksp7331.practice.libraryAPI.library.entity.Library;
import ksp7331.practice.libraryAPI.loan.entity.Loan;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    @OneToMany(mappedBy = "libraryMember")
    private List<Phone> phones = new ArrayList<>();
    @Builder
    public LibraryMember(Long id, Member member, Library library, String phone) {
        this.id = id;
        this.member = member;
        this.library = library;
        addPhone(phone);
    }

    public void addPhone(String phoneNumber) {
        Phone phone = Phone.builder()
                .number(phoneNumber)
                .libraryMember(this)
                .build();
        phones.add(phone);
    }
    @OneToMany(mappedBy = "libraryMember")
    private List<Loan> loans = new ArrayList<>();
}
