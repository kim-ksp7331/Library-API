package ksp7331.practice.libraryAPI.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@AllArgsConstructor
@Builder
public class Phone {
    private Long id;
    private String number;
    private LibraryMember libraryMember;
}
