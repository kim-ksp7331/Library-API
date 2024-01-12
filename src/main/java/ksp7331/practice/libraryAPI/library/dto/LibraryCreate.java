package ksp7331.practice.libraryAPI.library.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LibraryCreate {

    private String name;

    @Builder
    public LibraryCreate(String name) {
        this.name = name;
    }
}
