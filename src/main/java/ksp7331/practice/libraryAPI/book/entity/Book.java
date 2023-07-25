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

    @Builder
    public Book(Long id, String name, String author) {
        this.id = id;
        this.name = name;
        this.author = author;
    }
    @OneToMany(mappedBy = "book")
    public List<LibraryBook> libraryBooks = new ArrayList<>();

}
