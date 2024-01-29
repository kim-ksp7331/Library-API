package ksp7331.practice.libraryAPI.member.domain;

import lombok.*;


@Getter
@AllArgsConstructor
@Builder
public class Phone {
    private Long id;
    private String number;
    @Setter(AccessLevel.PACKAGE)
    private LibraryMember libraryMember;
}
