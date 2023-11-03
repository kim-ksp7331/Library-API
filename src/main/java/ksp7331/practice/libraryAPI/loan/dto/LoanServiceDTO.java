package ksp7331.practice.libraryAPI.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class LoanServiceDTO {
    @Getter
    @AllArgsConstructor
    @Builder
    public static class CreateParam {
        private List<Long> bookIds;
        private Long libraryMemberId;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Result {
        private Long id;
        private Long libraryMemberId;
        private String memberName;
        private String libraryName;
        private LocalDateTime createdDate;
        private List<Book> books;
        @Getter
        @AllArgsConstructor
        @Builder
        public static class Book {
            private String name;
            private String author;
        }
    }
}
