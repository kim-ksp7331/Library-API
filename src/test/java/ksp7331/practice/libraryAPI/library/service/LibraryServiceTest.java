package ksp7331.practice.libraryAPI.library.service;

import ksp7331.practice.libraryAPI.exception.BusinessLogicException;
import ksp7331.practice.libraryAPI.library.dto.LibraryServiceDTO;
import ksp7331.practice.libraryAPI.library.entity.Library;
import ksp7331.practice.libraryAPI.library.mapper.LibraryMapper;
import ksp7331.practice.libraryAPI.library.repository.LibraryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
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
    @Mock
    private LibraryMapper libraryMapper;
    @InjectMocks
    private LibraryService libraryService;
    @Test
    void createLibrary() {
        // given
        String libraryName = "newLib";
        Long id = 1L;
        given(libraryRepository.save(Mockito.any(Library.class)))
                .willReturn(Library.builder().id(id).build());

        // when
        Long resultId = libraryService.createLibrary(libraryName);
        // then
        assertThat(resultId).isEqualTo(id);
    }

    @Test
    void findLibraries() {
        // given
        int repeat = 3;
        String libName = "newLib";
        List<LibraryServiceDTO.Result> libraries = LongStream.rangeClosed(1, repeat).mapToObj(i -> LibraryServiceDTO.Result.builder()
                .id(i)
                .name(libName + i)
                .build()).collect(Collectors.toList());
        given(libraryMapper.entitiesToServiceDTOs(Mockito.anyList())).willReturn(libraries);

        // when
        List<LibraryServiceDTO.Result> results = libraryService.findLibraries();

        // then
        assertThat(results).hasSize(repeat);
        assertThat(results).allMatch(result -> result.getId() != null);
    }

    @Test
    void findVerifiedLibrary() {
        // given
        long id = 1L;
        Library library = Library.builder().id(id).build();
        given(libraryRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(library));

        // when
        Library result = libraryService.findVerifiedLibrary(id);

        // then
        assertThat(result).isEqualTo(library);
    }

    @Test
    void findVerifiedLibraryWhenNoLibrary() {
        // given
        long id = 1L;
        given(libraryRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());

        // when // then
        org.junit.jupiter.api.Assertions.assertThrows(BusinessLogicException.class, () -> libraryService.findVerifiedLibrary(id));
    }
}