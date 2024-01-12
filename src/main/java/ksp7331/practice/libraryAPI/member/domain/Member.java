package ksp7331.practice.libraryAPI.member.domain;


import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Member {
    private Long id;
    private String name;

    @Builder
    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    private List<LibraryMember> libraryMembers = new ArrayList<>();

    void addLibraryMember(LibraryMember libraryMember) {
        libraryMembers.add(libraryMember);
    }

    public static Member from(String name) {
        return Member.builder().name(name).build();
    }
}
