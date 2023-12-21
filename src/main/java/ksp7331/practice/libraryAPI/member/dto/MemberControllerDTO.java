package ksp7331.practice.libraryAPI.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberControllerDTO {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Post {
        private Long libraryMemberId;
        private String name;
        private Long libraryId;
        private String phoneNumber;
    }
}
