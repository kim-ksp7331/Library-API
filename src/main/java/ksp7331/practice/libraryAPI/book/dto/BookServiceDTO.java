package ksp7331.practice.libraryAPI.book.dto;

import ksp7331.practice.libraryAPI.library.dto.LibraryServiceDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

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

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Result {
        private Long bookId;
        private String name;
        private String author;
        private List<LibraryServiceDTO.Result> libraries;
    }
}
