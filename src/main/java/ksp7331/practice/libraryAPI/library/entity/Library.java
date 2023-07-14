package ksp7331.practice.libraryAPI.library.entity;

import ksp7331.practice.libraryAPI.member.entity.LibraryMember;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Library {
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

}
