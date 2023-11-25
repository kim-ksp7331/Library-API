package ksp7331.practice.libraryAPI.library.mapper;

import ksp7331.practice.libraryAPI.library.dto.LibraryControllerDTO;
import ksp7331.practice.libraryAPI.library.dto.LibraryServiceDTO;
import ksp7331.practice.libraryAPI.library.entity.Library;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LibraryMapper {
    public List<LibraryServiceDTO.Result> entitiesToServiceDTOs(List<Library> libraries) {
        return libraries.stream()
                .map(library -> LibraryServiceDTO.Result.builder().id(library.getId()).name(library.getName()).build())
                .collect(Collectors.toList());
    }

    public List<LibraryControllerDTO.Response> ServiceDTOsToControllerDTOs(List<LibraryServiceDTO.Result> results) {
        return results.stream()
                .map(result -> LibraryControllerDTO.Response.builder().id(result.getId()).name(result.getName()).build())
                .collect(Collectors.toList());
    }
}
