package ksp7331.practice.libraryAPI.library.controller;

import ksp7331.practice.libraryAPI.library.domain.Library;
import ksp7331.practice.libraryAPI.library.dto.LibraryCreate;
import ksp7331.practice.libraryAPI.library.dto.Response;
import ksp7331.practice.libraryAPI.library.service.LibraryService;
import ksp7331.practice.libraryAPI.util.UriCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/libraries")
@RequiredArgsConstructor
public class LibraryController {
    private static String LIBRARY_URL_PREFIX = "/libraries";
    private final LibraryService libraryService;
    @PostMapping
    public ResponseEntity<Void> postLibrary(@RequestBody LibraryCreate LibraryCreate) {
        Long id = libraryService.createLibrary(LibraryCreate);
        URI uri = UriCreator.createUri(LIBRARY_URL_PREFIX, id);
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<Response>> getLibraries() {
        List<Library> libraries = libraryService.findLibraries();
        return ResponseEntity.ok().body(libraries.stream().map(Response::from).collect(Collectors.toList()));
    }
}
