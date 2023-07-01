package dev.aest.siw.movie.model;

import org.springframework.data.domain.Page;

public record PageInfo(
        int number,
        boolean hasContent,
        boolean hasNext,
        boolean hasPrev,
        boolean isFirst,
        boolean isLast,
        long totalElements,
        long totalPages
)
{
    public static final String ATTRIBUTE_NAME = "pageInfo";

    public PageInfo(Page<?> page) {
        this(page.getNumber(),
                page.hasContent(),
                page.hasNext(),
                page.hasPrevious(),
                page.isFirst(),
                page.isLast(),
                page.getTotalElements(),
                page.getTotalPages());
    }
}
