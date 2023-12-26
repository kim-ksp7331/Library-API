package ksp7331.practice.libraryAPI.book.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Book {
    private Long id;
    private String name;
    private String author;
    private String publisher;

    @Builder
    public Book(Long id, String name, String author, String publisher) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.publisher = publisher;
    }
    private List<LibraryBook> libraryBooks = new ArrayList<>();

    public void addLibraryBook(LibraryBook libraryBook) {
        libraryBooks.add(libraryBook);
    }
}
