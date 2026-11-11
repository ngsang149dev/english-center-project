package com.abuenglishcenter.managementsystem.common;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * Stable list envelope for every paginated endpoint, so clients never depend on
 * Spring's Page JSON shape. Newest rows first (id descending).
 */
public record PageResponseDto<T>(List<T> items, int page, int size, long totalItems, int totalPages) {

    /** Safety cap for a single page, mirrors the controllers' @Max. */
    public static final int MAX_SIZE = 100;

    public static <E, T> PageResponseDto<T> of(Page<E> page, Function<E, T> mapper) {
        Page<T> mapped = page.map(mapper);
        return new PageResponseDto<>(mapped.getContent(), mapped.getNumber(), mapped.getSize(),
                mapped.getTotalElements(), mapped.getTotalPages());
    }

    public static PageRequest pageable(int page, int size) {
        return PageRequest.of(page, Math.min(size, MAX_SIZE), Sort.by(Sort.Direction.DESC, "id"));
    }
}
