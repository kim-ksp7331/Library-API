package ksp7331.practice.libraryAPI.member.infrastructure.entity;

import ksp7331.practice.libraryAPI.common.entity.BaseTimeEntity;
import ksp7331.practice.libraryAPI.member.domain.Phone;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "PHONE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PhoneEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 14)
    private String number;
    @ManyToOne
    @JoinColumn(name = "LIBRARY_MEMBER_ID")
    private LibraryMemberEntity libraryMember;

    public static PhoneEntity from(Phone domain, LibraryMemberEntity libraryMember) {
        PhoneEntity phone = new PhoneEntity();
        phone.id = domain.getId();
        phone.number = domain.getNumber();
        phone.libraryMember = libraryMember;
        return phone;
    }

    public Phone toDomain() {
        return Phone.builder()
                .id(id)
                .number(number)
                .build();
    }
}
