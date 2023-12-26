package ksp7331.practice.libraryAPI.book.infrastructure.entity;

import ksp7331.practice.libraryAPI.book.domain.BookState;
import ksp7331.practice.libraryAPI.common.entity.BaseTimeEntity;
import ksp7331.practice.libraryAPI.library.entity.Library;
import lombok.*;

import javax.persistence.*;
import java.util.Optional;

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
    private BookState state = BookState.LOANABLE;

    @Builder
    public LibraryBook(Long id, Book book, Library library, BookState state) {
        this.id = id;
        this.book = book;
        this.library = library;
        if(state != null) this.state = state;
    }
    public static LibraryBook from(ksp7331.practice.libraryAPI.book.domain.LibraryBook domain) {
        LibraryBook libraryBook = new LibraryBook();
        libraryBook.id = domain.getId();
        libraryBook.book = Book.from(domain.getBook());
        libraryBook.library = domain.getLibrary();
        if(domain.getState() != null) libraryBook.state = domain.getState();
        return libraryBook;
    }

    public ksp7331.practice.libraryAPI.book.domain.LibraryBook toDomain() {
        ksp7331.practice.libraryAPI.book.domain.Book domainBook = book.toDomainInternal();
        ksp7331.practice.libraryAPI.book.domain.LibraryBook domainLibraryBook = toDomainInternal(domainBook);
        return domainLibraryBook;
    }
    ksp7331.practice.libraryAPI.book.domain.LibraryBook toDomainInternal(ksp7331.practice.libraryAPI.book.domain.Book domainBook) {
        ksp7331.practice.libraryAPI.book.domain.LibraryBook domainLibraryBook = ksp7331.practice.libraryAPI.book.domain.LibraryBook.builder()
                .id(id)
                .book(domainBook)
                .library(library)
                .state(state)
                .build();
        domainBook.addLibraryBook(domainLibraryBook);
        return domainLibraryBook;
    }

    public void update(ksp7331.practice.libraryAPI.book.domain.LibraryBook libraryBook) {
        if(libraryBook == null) return;
        Optional.ofNullable(libraryBook.getState()).ifPresent(state -> this.state = state);
    }
}
