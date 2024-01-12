package ksp7331.practice.libraryAPI.member.infrastructure.entity;

import ksp7331.practice.libraryAPI.common.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Phone extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 14)
    private String number;
    @ManyToOne
    @JoinColumn(name = "LIBRARY_MEMBER_ID")
    private LibraryMember libraryMember;

    public static Phone from(ksp7331.practice.libraryAPI.member.domain.Phone domain, LibraryMember libraryMember) {
        Phone phone = new Phone();
        phone.id = domain.getId();
        phone.number = domain.getNumber();
        phone.libraryMember = libraryMember;
        return phone;
    }

    public ksp7331.practice.libraryAPI.member.domain.Phone toDomain() {
        return ksp7331.practice.libraryAPI.member.domain.Phone.builder()
                .id(id)
                .number(number)
                .build();
    }
}
