package ksp7331.practice.libraryAPI.book.mapper;

import ksp7331.practice.libraryAPI.book.dto.BookControllerDTO;
import ksp7331.practice.libraryAPI.book.dto.BookServiceDTO;
import ksp7331.practice.libraryAPI.book.entity.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class BookMapperTest {
    private BookMapper bookMapper = new BookMapper();

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
}