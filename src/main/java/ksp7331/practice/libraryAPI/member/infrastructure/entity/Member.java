package ksp7331.practice.libraryAPI.member.infrastructure.entity;

import ksp7331.practice.libraryAPI.common.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 20)
    private String name;

    @Builder
    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    @OneToMany(mappedBy = "member", cascade = {CascadeType.REMOVE})
    private List<LibraryMember> libraryMembers = new ArrayList<>();

    void addLibraryMember(LibraryMember libraryMember) {
        libraryMembers.add(libraryMember);
    }

    public static Member from(ksp7331.practice.libraryAPI.member.domain.Member domain) {
        Member member = new Member();
        member.id = domain.getId();
        member.name = domain.getName();
        return member;
    }

    public ksp7331.practice.libraryAPI.member.domain.Member toDomain() {
        return ksp7331.practice.libraryAPI.member.domain.Member.builder()
                .id(id)
                .name(name)
                .build();
    }
}
