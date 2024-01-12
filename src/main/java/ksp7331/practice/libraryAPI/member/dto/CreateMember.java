package ksp7331.practice.libraryAPI.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateMember {
    private Long libraryMemberId;
    private String name;
    private Long libraryId;
    private String phoneNumber;
}
