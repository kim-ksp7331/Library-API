package ksp7331.practice.libraryAPI.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class MemberControllerDTO {
    @Getter
    @AllArgsConstructor
    @Builder
    public static class Post {
        private Long libraryMemberId;
        private String name;
        private Long libraryId;
        private String phoneNumber;
    }
}
