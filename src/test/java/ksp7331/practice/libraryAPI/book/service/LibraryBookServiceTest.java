package ksp7331.practice.libraryAPI.book.service;

import ksp7331.practice.libraryAPI.book.domain.Book;
import ksp7331.practice.libraryAPI.book.service.port.LibraryBookRepository;
import ksp7331.practice.libraryAPI.library.entity.Library;
import ksp7331.practice.libraryAPI.library.service.LibraryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

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
        Long bookId = 1L;
        long libraryId = 1L;
        Book book = Book.builder().id(bookId).build();

        BDDMockito.given(libraryService.findVerifiedLibrary(Mockito.anyLong()))
                .willReturn(Library.builder().id(libraryId).build());

        // when // then
        assertThatCode(() -> libraryBookService.createLibraryBook(book, libraryId)).doesNotThrowAnyException();
    }
}