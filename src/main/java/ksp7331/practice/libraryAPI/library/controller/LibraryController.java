package ksp7331.practice.libraryAPI.library.controller;

import ksp7331.practice.libraryAPI.library.dto.LibraryControllerDTO;
import ksp7331.practice.libraryAPI.library.service.LibraryService;
import ksp7331.practice.libraryAPI.util.UriCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/libraries")
@RequiredArgsConstructor
public class LibraryController {
    private static String LIBRARY_URL_PREFIX = "/libraries/";
    private final LibraryService libraryService;
    @PostMapping
    public ResponseEntity<Void> postLibrary(@RequestBody LibraryControllerDTO.Post post) {
        Long id = libraryService.createLibrary(post.getName());
        URI uri = UriCreator.createUri(LIBRARY_URL_PREFIX, id);
        return ResponseEntity.created(uri).build();
    }
}
