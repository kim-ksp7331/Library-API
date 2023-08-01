package ksp7331.practice.libraryAPI.book.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class BookControllerDTO {
    @Getter
    @AllArgsConstructor
    @Builder
    public static class Post {
        private String name;
        private String author;
    }
}
