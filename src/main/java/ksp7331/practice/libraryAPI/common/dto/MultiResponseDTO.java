package ksp7331.practice.libraryAPI.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class MultiResponseDTO<T> {

    private List<T> data;
    private PageInfo pageInfo;

    @Builder
    public MultiResponseDTO(List<T> data, Page page) {
        this.data = data;
        this.pageInfo = new PageInfo(page.getNumber() + 1,
                page.getSize(), page.getTotalElements(), page.getTotalPages());
    }
    @AllArgsConstructor
    @Getter
    public class PageInfo {
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
    }
}
