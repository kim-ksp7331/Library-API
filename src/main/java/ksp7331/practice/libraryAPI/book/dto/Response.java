package ksp7331.practice.libraryAPI.book.dto;

import ksp7331.practice.libraryAPI.book.domain.Book;
import ksp7331.practice.libraryAPI.library.domain.Library;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class Response {
    private Long bookId;
    private String name;
    private String author;
    private String publisher;
    private List<ksp7331.practice.libraryAPI.library.dto.Response> libraries;

    public static Response from(Book book) {
        return Response.builder()
                .bookId(book.getId())
                .name(book.getName())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .libraries(book.getLibraryBooks().stream().map(libraryBook -> {
                    Library library = libraryBook.getLibrary();
                    return ksp7331.practice.libraryAPI.library.dto.Response.builder()
                            .id(library.getId())
                            .name(library.getName())
                            .build();
                }).collect(Collectors.toList()))
                .build();
    }
}
