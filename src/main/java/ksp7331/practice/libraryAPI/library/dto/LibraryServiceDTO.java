package ksp7331.practice.libraryAPI.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class LibraryServiceDTO {
    @Getter
    @AllArgsConstructor
    @Builder
    public static class Result {
        private Long id;
        private String name;
    }
}
