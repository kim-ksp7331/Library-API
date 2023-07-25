package ksp7331.practice.libraryAPI.book.mapper;

import ksp7331.practice.libraryAPI.book.dto.BookControllerDTO;
import ksp7331.practice.libraryAPI.book.dto.BookServiceDTO;
import ksp7331.practice.libraryAPI.book.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {
    public BookServiceDTO.CreateParam controllerDTOToServiceDTO(BookControllerDTO.Post post, Long libraryId) {
        return BookServiceDTO.CreateParam.builder()
                .name(post.getName())
                .author(post.getAuthor())
                .libraryId(libraryId)
                .build();
    }
    public Book ServiceDTOToEntity(BookServiceDTO.CreateParam createParam) {
        return Book.builder()
                .name(createParam.getName())
                .author(createParam.getAuthor())
                .build();
    }
}
