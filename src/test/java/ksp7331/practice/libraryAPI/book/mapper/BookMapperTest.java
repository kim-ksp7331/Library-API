package ksp7331.practice.libraryAPI.book.mapper;

import ksp7331.practice.libraryAPI.book.dto.BookControllerDTO;
import ksp7331.practice.libraryAPI.book.dto.BookServiceDTO;
import ksp7331.practice.libraryAPI.book.entity.Book;
import ksp7331.practice.libraryAPI.book.entity.LibraryBook;
import ksp7331.practice.libraryAPI.library.dto.LibraryControllerDTO;
import ksp7331.practice.libraryAPI.library.dto.LibraryServiceDTO;
import ksp7331.practice.libraryAPI.library.entity.Library;
import ksp7331.practice.libraryAPI.library.mapper.LibraryMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookMapperTest {
    @InjectMocks
    private BookMapper bookMapper;
    @Mock
    private LibraryMapper libraryMapper;

    @Test
    void controllerDTOToServiceDTO() {
        // given
        String name = "book1";
        String author = "author1";
        Long libraryId = 2L;
        BookControllerDTO.Post post = BookControllerDTO.Post.builder().name(name).author(author).build();

        // when
        BookServiceDTO.CreateParam createParam = bookMapper.controllerDTOToServiceDTO(post, libraryId);

        // then
        assertThat(createParam.getName()).isEqualTo(name);
        assertThat(createParam.getAuthor()).isEqualTo(author);
        assertThat(createParam.getLibraryId()).isEqualTo(libraryId);
    }

    @Test
    void serviceDTOToEntity() {
        // given
        String name = "book1";
        String author = "author1";
        long libraryId = 1L;
        BookServiceDTO.CreateParam createParam = BookServiceDTO.CreateParam
                .builder().name(name).author(author).libraryId(libraryId).build();
        // when
        Book book = bookMapper.ServiceDTOToEntity(createParam);

        // then
        assertThat(book.getName()).isEqualTo(name);
        assertThat(book.getAuthor()).isEqualTo(author);
    }
    @Test
    void EntityToServiceDTO() {
        // given
        long bookId = 1L;
        String bookName = "book1";
        String author = "author1";
        long libraryId = 1L;
        String libraryName = "lib1";
        Book book = Book.builder().id(bookId).name(bookName).author(author).build();
        Library library = Library.builder().id(libraryId).name(libraryName).build();
        LibraryServiceDTO.Result libraryResult = LibraryServiceDTO.Result.builder().id(libraryId).name(libraryName).build();
        LibraryBook.builder().book(book).library(library).build();

        BDDMockito.given(libraryMapper.entitiesToServiceDTOs(Mockito.anyList())).willReturn(List.of(libraryResult));

        // when
        BookServiceDTO.Result result = bookMapper.EntityToServiceDTO(book);

        // then
        assertThat(result.getBookId()).isEqualTo(bookId);
        assertThat(result.getName()).isEqualTo(bookName);
        assertThat(result.getAuthor()).isEqualTo(author);
        assertThat(result.getLibraries().get(0)).isEqualTo(libraryResult);
    }

    @Test
    void ServiceDTOToControllerDTO() {
        // given
        long bookId = 1L;
        String bookName = "book1";
        String author = "author1";
        long libraryId = 1L;
        String libraryName = "lib1";
        LibraryServiceDTO.Result libraryResult = LibraryServiceDTO.Result.builder().id(libraryId).name(libraryName).build();
        LibraryControllerDTO.Response libraryResponse = LibraryControllerDTO.Response.builder()
                .id(libraryId).name(libraryName).build();
        BookServiceDTO.Result result = BookServiceDTO.Result.builder()
                .bookId(bookId).name(bookName).author(author).libraries(List.of(libraryResult)).build();

        BDDMockito.given(libraryMapper.ServiceDTOsToControllerDTOs(Mockito.anyList())).willReturn(List.of(libraryResponse));

        // when
        BookControllerDTO.Response response = bookMapper.ServiceDTOToControllerDTO(result);

        // then
        assertThat(response.getBookId()).isEqualTo(bookId);
        assertThat(response.getName()).isEqualTo(bookName);
        assertThat(response.getAuthor()).isEqualTo(author);
        assertThat(response.getLibraries().get(0)).isEqualTo(libraryResponse);

    }
}