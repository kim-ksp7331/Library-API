package ksp7331.practice.libraryAPI.library.service;

import ksp7331.practice.libraryAPI.library.entity.Library;
import ksp7331.practice.libraryAPI.library.repository.LibraryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {

    @Mock
    private LibraryRepository libraryRepository;
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
        Assertions.assertThat(resultId).isEqualTo(id);
    }
}