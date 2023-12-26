package ksp7331.practice.libraryAPI.book.infrastructure.entity;

import ksp7331.practice.libraryAPI.common.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 30)
    private String name;
    @Column(nullable = false, length = 20)
    private String author;
    @Column(nullable = false, length = 20)
    private String publisher;

    @Builder
    public Book(Long id, String name, String author, String publisher) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.publisher = publisher;
    }
    @OneToMany(mappedBy = "book")
    private List<LibraryBook> libraryBooks = new ArrayList<>();

    void addLibraryBook(LibraryBook libraryBook) {
        libraryBooks.add(libraryBook);
    }
    public static Book from(ksp7331.practice.libraryAPI.book.domain.Book domain) {
        Book book = new Book();
        book.id = domain.getId();
        book.name = domain.getName();
        book.author = domain.getAuthor();
        book.publisher = domain.getPublisher();
        return book;
    }

    public ksp7331.practice.libraryAPI.book.domain.Book toDomain() {
        ksp7331.practice.libraryAPI.book.domain.Book domain = toDomainInternal();
        libraryBooks.forEach(libraryBook -> {
            libraryBook.toDomainInternal(domain);
        });
        return domain;
    }
    ksp7331.practice.libraryAPI.book.domain.Book toDomainInternal() {
        ksp7331.practice.libraryAPI.book.domain.Book domainBook = ksp7331.practice.libraryAPI.book.domain.Book.builder()
                .id(id)
                .name(name)
                .author(author)
                .publisher(publisher)
                .build();
        return domainBook;
    }

}
