package ksp7331.practice.libraryAPI.book.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BookPageCreate {
    private int page;
    private int size;
}
