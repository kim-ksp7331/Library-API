package ksp7331.practice.libraryAPI.book.domain;

import ksp7331.practice.libraryAPI.library.domain.Library;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class LibraryBook {
    private Long id;
    private Book book;
    private Library library;
    @Setter
    private BookState state = BookState.LOANABLE;

    @Builder
    public LibraryBook(Long id, Book book, Library library, BookState state) {
        this.id = id;
        this.book = book;
        this.library = library;
        if(state != null) this.state = state;
    }
}
