package ksp7331.practice.libraryAPI.book.controller;

import ksp7331.practice.libraryAPI.book.dto.BookControllerDTO;
import ksp7331.practice.libraryAPI.book.dto.BookServiceDTO;
import ksp7331.practice.libraryAPI.book.mapper.BookMapper;
import ksp7331.practice.libraryAPI.book.service.BookService;
import ksp7331.practice.libraryAPI.util.UriCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final BookMapper bookMapper;

    @PostMapping("/libraries/{library-id}/books")
    public ResponseEntity<Void> postNewBook(@PathVariable("library-id")Long libraryId, @RequestBody BookControllerDTO.Post post) {
        String BOOK_URL_PREFIX = "/books";

        Long id = bookService.createNewBook(bookMapper.controllerDTOToServiceDTO(post, libraryId));
        URI uri = UriCreator.createUri(BOOK_URL_PREFIX, id);
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/libraries/{library-id}/books/{book-id}")
    public ResponseEntity<Void> postBook(@PathVariable("library-id")Long libraryId, @PathVariable("book-id")Long bookId) {
        bookService.addBookToLibrary(BookServiceDTO.AddParam.builder()
                        .bookId(bookId)
                        .libraryId(libraryId)
                        .build());
        return ResponseEntity.ok().build();
    }
}
