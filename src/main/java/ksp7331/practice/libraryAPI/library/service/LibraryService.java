package ksp7331.practice.libraryAPI.library.service;

import ksp7331.practice.libraryAPI.library.dto.LibraryServiceDTO;
import ksp7331.practice.libraryAPI.library.entity.Library;
import ksp7331.practice.libraryAPI.library.mapper.LibraryMapper;
import ksp7331.practice.libraryAPI.library.repository.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class LibraryService {
    private final LibraryRepository libraryRepository;
    private final LibraryMapper libraryMapper;

    public Long createLibrary(String libraryName) {
        Library library = Library.builder()
                .name(libraryName)
                .build();
        return libraryRepository.save(library).getId();
    }

    @Transactional(readOnly = true)
    public List<LibraryServiceDTO.Result> findLibraries() {
        List<Library> libraries = libraryRepository.findAll();
        return libraryMapper.entitiesToServiceDTOs(libraries);
    }
}
