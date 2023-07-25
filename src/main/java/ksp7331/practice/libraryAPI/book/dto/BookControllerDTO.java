package ksp7331.practice.libraryAPI.book.dto;

import lombok.Getter;

public class BookControllerDTO {
    @Getter
    public static class Post {
        private String name;
        private String author;
    }
}
