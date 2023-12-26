package ksp7331.practice.libraryAPI.book.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ksp7331.practice.libraryAPI.book.domain.Book;
import lombok.*;

@Getter
@NoArgsConstructor
public class BookCreate {
    private String name;
    private String author;
    private String publisher;
    @Builder
    public BookCreate(String name, String author, String publisher) {
        this.name = name;
        this.author = author;
        this.publisher = publisher;
    }

    @JsonIgnore
    @Setter
    private Long libraryId;
    public Book toDomain() {
        return Book.builder()
                .name(name)
                .publisher(publisher)
                .author(author)
                .build();
    }
}
