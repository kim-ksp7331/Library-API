package ksp7331.practice.libraryAPI.book.entity;

import ksp7331.practice.libraryAPI.book.entity.Book;
import ksp7331.practice.libraryAPI.common.entity.BaseTimeEntity;
import ksp7331.practice.libraryAPI.library.entity.Library;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LibraryBook extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "BOOK_ID")
    private Book book;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LIBRARY_ID")
    private Library library;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Setter
    private State state = State.LOANABLE;

    @Builder
    public LibraryBook(Book book, Library library) {
        this.book = book;
        this.library = library;
    }

    public enum State {
        LOANABLE, NOT_LOANABLE
    }
}
