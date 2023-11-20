package ksp7331.practice.libraryAPI.book.dto;

import ksp7331.practice.libraryAPI.library.dto.LibraryControllerDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class BookControllerDTO {
    @Getter
    @AllArgsConstructor
    @Builder
    public static class Post {
        private String name;
        private String author;
        private String publisher;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long bookId;
        private String name;
        private String author;
        private String publisher;
        private List<LibraryControllerDTO.Response> libraries;
    }
}
