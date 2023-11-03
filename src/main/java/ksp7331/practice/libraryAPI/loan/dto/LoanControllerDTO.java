package ksp7331.practice.libraryAPI.loan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

public class LoanControllerDTO {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Post {
        private List<Long> bookIds;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private Long libraryMemberId;
        private String memberName;
        private String libraryName;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
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
