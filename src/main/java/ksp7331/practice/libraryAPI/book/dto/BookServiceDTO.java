package ksp7331.practice.libraryAPI.book.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class BookServiceDTO {
    @Getter
    @AllArgsConstructor
    @Builder
    public static class CreateParam {
        private String name;
        private String author;
        private Long libraryId;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class AddParam {
        private Long bookId;
        private Long libraryId;
    }
}
