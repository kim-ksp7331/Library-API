package ksp7331.practice.libraryAPI.book.service;

import ksp7331.practice.libraryAPI.book.entity.Book;
import ksp7331.practice.libraryAPI.book.entity.LibraryBook;
import ksp7331.practice.libraryAPI.book.repository.LibraryBookRepository;
import ksp7331.practice.libraryAPI.library.entity.Library;
import ksp7331.practice.libraryAPI.library.service.LibraryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LibraryBookServiceTest {
    @InjectMocks
    private LibraryBookService libraryBookService;
    @Mock
    private LibraryBookRepository libraryBookRepository;
    @Mock
    private LibraryService libraryService;

    @DisplayName("library에 book 등록")
    @Test
    void createLibraryBook() {
        // given
        Book book = Book.builder().build();
        long libraryId = 1L;

        BDDMockito.given(libraryService.findVerifiedLibrary(Mockito.anyLong()))
                .willReturn(Library.builder().id(libraryId).build());

        // when // then
        assertThatCode(() -> libraryBookService.createLibraryBook(book, libraryId)).doesNotThrowAnyException();
    }
}