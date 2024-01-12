package ksp7331.practice.libraryAPI.library.service;

import ksp7331.practice.libraryAPI.exception.BusinessLogicException;
import ksp7331.practice.libraryAPI.exception.ExceptionCode;
import ksp7331.practice.libraryAPI.library.domain.Library;
import ksp7331.practice.libraryAPI.library.dto.LibraryCreate;
import ksp7331.practice.libraryAPI.library.service.port.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LibraryService {
    private final LibraryRepository libraryRepository;

    public Long createLibrary(LibraryCreate libraryCreate) {
        Library library = Library.from(libraryCreate);
        return libraryRepository.create(library).getId();
    }

    @Transactional(readOnly = true)
    public List<Library> findLibraries() {
        return libraryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Library getById(Long id) {
        Optional<Library> optionalLibrary = libraryRepository.findById(id);
        return optionalLibrary.orElseThrow(() -> new BusinessLogicException(ExceptionCode.LIBRARY_NOT_FOUND));
    }
}
