package ksp7331.practice.libraryAPI.book.mapper;

import ksp7331.practice.libraryAPI.book.dto.BookControllerDTO;
import ksp7331.practice.libraryAPI.book.dto.BookServiceDTO;
import ksp7331.practice.libraryAPI.book.entity.Book;
import ksp7331.practice.libraryAPI.library.mapper.LibraryMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BookMapper {
    private final LibraryMapper libraryMapper;

    public BookMapper(LibraryMapper libraryMapper) {
        this.libraryMapper = libraryMapper;
    }

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

    public BookServiceDTO.Result EntityToServiceDTO(Book book) {
        return BookServiceDTO.Result.builder()
                .bookId(book.getId())
                .name(book.getName())
                .author(book.getAuthor())
                .libraries(libraryMapper.entitiesToServiceDTOs(book.getLibraryBooks().stream()
                        .map(lb -> lb.getLibrary()).collect(Collectors.toList())))
                .build();
    }

    public BookControllerDTO.Response ServiceDTOToControllerDTO(BookServiceDTO.Result result) {
        return BookControllerDTO.Response.builder()
                .bookId(result.getBookId())
                .name(result.getName())
                .author(result.getAuthor())
                .libraries(libraryMapper.ServiceDTOsToControllerDTOs(result.getLibraries()))
                .build();
    }
}
