package ksp7331.practice.libraryAPI.library.service;

import ksp7331.practice.libraryAPI.exception.BusinessLogicException;
import ksp7331.practice.libraryAPI.library.domain.Library;
import ksp7331.practice.libraryAPI.library.dto.LibraryCreate;
import ksp7331.practice.libraryAPI.library.service.port.LibraryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {

    @Mock
    private LibraryRepository libraryRepository;
    @InjectMocks
    private LibraryService libraryService;

    @DisplayName("library 엔티티 생성")
    @Test
    void createLibrary() {
        // given
        String libraryName = "newLib";
        LibraryCreate libraryCreate = LibraryCreate.builder().name(libraryName).build();
        Long id = 1L;
        given(libraryRepository.create(Mockito.any(Library.class)))
                .willReturn(Library.builder().id(id).build());

        // when
        Long resultId = libraryService.createLibrary(libraryCreate);
        // then
        assertThat(resultId).isEqualTo(id);
    }

    @DisplayName("library 엔티티 전체 조회")
    @Test
    void findLibraries() {
        // given
        int repeat = 3;
        String libName = "newLib";
        List<Library> libraries = LongStream.rangeClosed(1, repeat).mapToObj(i -> Library.builder()
                .id(i)
                .name(libName + i)
                .build()).collect(Collectors.toList());
        given(libraryRepository.findAll()).willReturn(libraries);

        // when
        List<Library> results = libraryService.findLibraries();

        // then
        assertThat(results).hasSize(repeat);
        assertThat(results).allMatch(result -> result.getId() != null);
    }

    @DisplayName("library 엔티티가 존재할 때 조회")
    @Test
    void findVerifiedLibrary() {
        // given
        long id = 1L;
        Library library = Library.builder().id(id).build();
        given(libraryRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(library));

        // when
        Library result = libraryService.getById(id);

        // then
        assertThat(result).isEqualTo(library);
    }

    @DisplayName("library 엔티티가 존재하지 않을 때 조회")
    @Test
    void findVerifiedLibraryWhenNoLibrary() {
        // given
        long id = 1L;
        given(libraryRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());

        // when // then
        org.junit.jupiter.api.Assertions.assertThrows(BusinessLogicException.class, () -> libraryService.getById(id));
    }
}