package ksp7331.practice.libraryAPI.book.infrastructure.entity;

import ksp7331.practice.libraryAPI.book.domain.Book;
import ksp7331.practice.libraryAPI.book.domain.BookState;
import ksp7331.practice.libraryAPI.book.domain.LibraryBook;
import ksp7331.practice.libraryAPI.common.entity.BaseTimeEntity;
import ksp7331.practice.libraryAPI.library.infrastructure.entity.LibraryEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Table(name = "LIBRARY_BOOK")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LibraryBookEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "BOOK_ID")
    private BookEntity book;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LIBRARY_ID")
    private LibraryEntity library;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Setter
    private BookState state = BookState.LOANABLE;

    @Builder
    public LibraryBookEntity(Long id, BookEntity book, LibraryEntity library, BookState state) {
        this.id = id;
        this.book = book;
        this.library = library;
        if(state != null) this.state = state;
    }
    public static LibraryBookEntity from(LibraryBook domain) {
        LibraryBookEntity libraryBook = new LibraryBookEntity();
        libraryBook.id = domain.getId();
        libraryBook.book = BookEntity.from(domain.getBook());
        libraryBook.library = LibraryEntity.from(domain.getLibrary());
        if(domain.getState() != null) libraryBook.state = domain.getState();
        return libraryBook;
    }

    public LibraryBook toDomain() {
        Book domainBook = book.toDomainSub();
        LibraryBook domainLibraryBook = toDomainSub(domainBook);
        return domainLibraryBook;
    }
    LibraryBook toDomainSub(Book domainBook) {
        LibraryBook domainLibraryBook = LibraryBook.builder()
                .id(id)
                .book(domainBook)
                .library(library.toDomain())
                .state(state)
                .build();
        domainBook.addLibraryBook(domainLibraryBook);
        return domainLibraryBook;
    }

    public void update(LibraryBook libraryBook) {
        if(libraryBook == null) return;
        Optional.ofNullable(libraryBook.getState()).ifPresent(state -> this.state = state);
    }
}
