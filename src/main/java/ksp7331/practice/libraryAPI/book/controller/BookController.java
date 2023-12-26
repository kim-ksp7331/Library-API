package ksp7331.practice.libraryAPI.book.controller;

import ksp7331.practice.libraryAPI.book.dto.*;
import ksp7331.practice.libraryAPI.book.service.BookService;
import ksp7331.practice.libraryAPI.common.dto.MultiResponseDTO;
import ksp7331.practice.libraryAPI.util.UriCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    @PostMapping("/libraries/{library-id}/books")
    public ResponseEntity<Void> postNewBook(@PathVariable("library-id")Long libraryId, @RequestBody BookCreate bookCreate) {
        String BOOK_URL_PREFIX = "/books";
        bookCreate.setLibraryId(libraryId);
        Long id = bookService.createNewBook(bookCreate);
        URI uri = UriCreator.createUri(BOOK_URL_PREFIX, id);
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/libraries/{library-id}/books/{book-id}")
    public ResponseEntity<Void> postBook(@PathVariable("library-id")Long libraryId, @PathVariable("book-id")Long bookId) {
        bookService.addBookToLibrary(LibraryBookCreate.builder()
                        .bookId(bookId)
                        .libraryId(libraryId)
                        .build());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/books/{book-id}")
    public ResponseEntity<Response> getBook(@PathVariable("book-id")Long bookId) {
        Response response = Response.from(bookService.getById(bookId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/books")
    public ResponseEntity<MultiResponseDTO<Response>> getBooks(@ModelAttribute BookPageCreate dto) {
        Page<Response> books = bookService.findAll(dto).map(Response::from);
        return ResponseEntity.ok(MultiResponseDTO.<Response>builder().page(books).data(books.getContent()).build());
    }
}
