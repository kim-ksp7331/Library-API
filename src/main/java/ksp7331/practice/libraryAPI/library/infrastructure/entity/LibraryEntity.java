package ksp7331.practice.libraryAPI.library.infrastructure.entity;

import ksp7331.practice.libraryAPI.book.infrastructure.entity.LibraryBookEntity;
import ksp7331.practice.libraryAPI.common.entity.BaseTimeEntity;
import ksp7331.practice.libraryAPI.library.domain.Library;
import ksp7331.practice.libraryAPI.member.infrastructure.entity.LibraryMemberEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "LIBRARY")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LibraryEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 20)
    private String name;

    @Builder
    public LibraryEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @OneToMany(mappedBy = "library")
    private List<LibraryMemberEntity> libraryMembers = new ArrayList<>();

    @OneToMany(mappedBy = "library")
    private List<LibraryBookEntity> libraryBooks = new ArrayList<>();

    public static LibraryEntity from(Library domain) {
        LibraryEntity library = new LibraryEntity();
        library.id = domain.getId();
        library.name = domain.getName();
        return library;
    }

    public Library toDomain() {
        return Library.builder()
                .id(id)
                .name(name)
                .build();
    }

}
