package ksp7331.practice.libraryAPI.library.service;

import ksp7331.practice.libraryAPI.library.entity.Library;
import ksp7331.practice.libraryAPI.library.repository.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LibraryService {
    private final LibraryRepository libraryRepository;

    public Long createLibrary(String libraryName) {
        Library library = Library.builder()
                .name(libraryName)
                .build();
        return libraryRepository.save(library).getId();
    }
}
