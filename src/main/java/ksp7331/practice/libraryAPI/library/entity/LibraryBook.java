package ksp7331.practice.libraryAPI.library.entity;

import ksp7331.practice.libraryAPI.book.entity.Book;
import ksp7331.practice.libraryAPI.library.entity.Library;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LibraryBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "BOOK_ID")
    private Book book;
    @ManyToOne
    @JoinColumn(name = "LIBRARY_ID")
    private Library library;
}
