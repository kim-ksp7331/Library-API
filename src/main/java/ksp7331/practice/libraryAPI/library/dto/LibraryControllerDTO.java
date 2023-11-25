package ksp7331.practice.libraryAPI.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class LibraryControllerDTO {
    @Getter
    public static class Post {
        private String name;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String name;
    }
}
