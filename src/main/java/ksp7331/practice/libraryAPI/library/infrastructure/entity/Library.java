package ksp7331.practice.libraryAPI.library.infrastructure.entity;

import ksp7331.practice.libraryAPI.book.infrastructure.entity.LibraryBook;
import ksp7331.practice.libraryAPI.common.entity.BaseTimeEntity;
import ksp7331.practice.libraryAPI.member.infrastructure.entity.LibraryMember;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Library extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 20)
    private String name;

    @Builder
    public Library(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @OneToMany(mappedBy = "library")
    private List<LibraryMember> libraryMembers = new ArrayList<>();

    @OneToMany(mappedBy = "library")
    private List<LibraryBook> libraryBooks = new ArrayList<>();

    public static Library from(ksp7331.practice.libraryAPI.library.domain.Library domain) {
        Library library = new Library();
        library.id = domain.getId();
        library.name = domain.getName();
        return library;
    }

    public ksp7331.practice.libraryAPI.library.domain.Library toDomain() {
        return ksp7331.practice.libraryAPI.library.domain.Library.builder()
                .id(id)
                .name(name)
                .build();
    }

}
