package ksp7331.practice.libraryAPI.member.infrastructure.entity;

import ksp7331.practice.libraryAPI.common.entity.BaseTimeEntity;
import ksp7331.practice.libraryAPI.member.domain.Member;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MEMBER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 20)
    private String name;

    @Builder
    public MemberEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    @OneToMany(mappedBy = "member", cascade = {CascadeType.REMOVE})
    private List<LibraryMemberEntity> libraryMembers = new ArrayList<>();

    void addLibraryMember(LibraryMemberEntity libraryMember) {
        libraryMembers.add(libraryMember);
    }

    public static MemberEntity from(Member domain) {
        MemberEntity member = new MemberEntity();
        member.id = domain.getId();
        member.name = domain.getName();
        return member;
    }

    public Member toDomain() {
        return Member.builder()
                .id(id)
                .name(name)
                .build();
    }
}
