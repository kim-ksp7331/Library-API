package ksp7331.practice.libraryAPI.library.mapper;

import ksp7331.practice.libraryAPI.library.dto.LibraryControllerDTO;
import ksp7331.practice.libraryAPI.library.dto.LibraryServiceDTO;
import ksp7331.practice.libraryAPI.library.entity.Library;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class LibraryMapperTest {
    private LibraryMapper libraryMapper = new LibraryMapper();
    @Test
    void entitiesToServiceDTOs() {
        // given
        int repeat = 3;
        String libName = "newLib";
        List<Library> libraries = LongStream.rangeClosed(1, repeat).mapToObj(i -> Library.builder()
                .id(i)
                .name(libName + i)
                .build()).collect(Collectors.toList());
        // when
        List<LibraryServiceDTO.Result> results = libraryMapper.entitiesToServiceDTOs(libraries);

        // then
        IntStream.rangeClosed(0, repeat - 1).forEach(i -> {
            assertThat(results)
                    .element(i)
                    .matches(result -> result.getId() == libraries.get(i).getId())
                    .matches(result -> result.getName().equals(libraries.get(i).getName()));
        });
    }

    @Test
    void ServiceDTOsToControllerDTOs() {
        // given
        int repeat = 3;
        String libName = "newLib";
        List<LibraryServiceDTO.Result> libraries = LongStream.rangeClosed(1, repeat).mapToObj(i -> LibraryServiceDTO.Result.builder()
                .id(i)
                .name(libName + i)
                .build()).collect(Collectors.toList());
        // when
        List<LibraryControllerDTO.Response> responses = libraryMapper.ServiceDTOsToControllerDTOs(libraries);

        // then
        IntStream.rangeClosed(0, repeat - 1).forEach(i -> {
            assertThat(responses)
                    .element(i)
                    .matches(result -> result.getId() == libraries.get(i).getId())
                    .matches(result -> result.getName().equals(libraries.get(i).getName()));
        });
    }
}