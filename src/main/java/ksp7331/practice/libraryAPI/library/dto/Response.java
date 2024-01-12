package ksp7331.practice.libraryAPI.library.dto;

import ksp7331.practice.libraryAPI.library.domain.Library;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class Response {
    private Long id;
    private String name;

    public static Response from(Library library) {
        return Response.builder()
                .id(library.getId())
                .name(library.getName())
                .build();
    }
}
