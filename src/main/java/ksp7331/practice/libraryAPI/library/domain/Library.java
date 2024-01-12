package ksp7331.practice.libraryAPI.library.domain;

import ksp7331.practice.libraryAPI.book.domain.LibraryBook;
import ksp7331.practice.libraryAPI.library.dto.LibraryCreate;
import ksp7331.practice.libraryAPI.member.domain.LibraryMember;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Library {
    private Long id;
    private String name;

    @Builder
    public Library(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    private List<LibraryMember> libraryMembers = new ArrayList<>();

    private List<LibraryBook> libraryBooks = new ArrayList<>();

    public static Library from(LibraryCreate libraryCreate) {
        return Library.builder().name(libraryCreate.getName()).build();
    }
}
