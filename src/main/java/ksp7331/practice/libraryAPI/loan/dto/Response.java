package ksp7331.practice.libraryAPI.loan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ksp7331.practice.libraryAPI.loan.domain.Loan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class Response {
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
        private Long id;
        private LocalDateTime returnDate;
        private String name;
        private String author;
        private String publisher;
        private String state;
    }

    public static Response from(Loan loan) {
        List<Response.Book> books = loan.getLoanBooks().stream().map(loanBook -> {
            ksp7331.practice.libraryAPI.book.entity.Book book = loanBook.getLibraryBook().getBook();
            return Response.Book.builder()
                    .id(book.getId())
                    .state(loanBook.getState().name())
                    .name(book.getName())
                    .author(book.getAuthor())
                    .publisher(book.getPublisher())
                    .returnDate(loanBook.getReturnDate())
                    .build();
        }).collect(Collectors.toList());
        return Response.builder()
                .id(loan.getId())
                .createdDate(loan.getCreatedDate())
                .libraryName(loan.getLibraryMember().getLibrary().getName())
                .memberName(loan.getLibraryMember().getMember().getName())
                .libraryMemberId(loan.getLibraryMember().getId())
                .books(books)
                .build();
    }

}