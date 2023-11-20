package ksp7331.practice.libraryAPI.book.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book {
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

}
