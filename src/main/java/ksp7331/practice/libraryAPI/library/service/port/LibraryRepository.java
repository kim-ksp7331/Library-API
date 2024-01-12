package ksp7331.practice.libraryAPI.library.service.port;

import ksp7331.practice.libraryAPI.library.domain.Library;

import java.util.List;
import java.util.Optional;

public interface LibraryRepository {
    Library create(Library library);

    List<Library> findAll();

    Optional<Library> findById(Long id);
}
