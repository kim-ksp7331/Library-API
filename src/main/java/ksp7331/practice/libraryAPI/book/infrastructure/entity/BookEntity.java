package ksp7331.practice.libraryAPI.book.infrastructure.entity;

import ksp7331.practice.libraryAPI.book.domain.Book;
import ksp7331.practice.libraryAPI.common.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BOOK")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookEntity extends BaseTimeEntity {
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
    public BookEntity(Long id, String name, String author, String publisher) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.publisher = publisher;
    }
    @OneToMany(mappedBy = "book")
    private List<LibraryBookEntity> libraryBooks = new ArrayList<>();

    void addLibraryBook(LibraryBookEntity libraryBook) {
        libraryBooks.add(libraryBook);
    }
    public static BookEntity from(Book domain) {
        BookEntity book = new BookEntity();
        book.id = domain.getId();
        book.name = domain.getName();
        book.author = domain.getAuthor();
        book.publisher = domain.getPublisher();
        return book;
    }

    public Book toDomain() {
        Book domain = toDomainSub();
        libraryBooks.forEach(libraryBook -> {
            libraryBook.toDomainSub(domain);
        });
        return domain;
    }
    Book toDomainSub() {
        Book domainBook = Book.builder()
                .id(id)
                .name(name)
                .author(author)
                .publisher(publisher)
                .build();
        return domainBook;
    }

}
