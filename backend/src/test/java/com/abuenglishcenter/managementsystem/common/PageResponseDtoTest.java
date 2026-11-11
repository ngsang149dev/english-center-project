package com.abuenglishcenter.managementsystem.common;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

class PageResponseDtoTest {

    @Test
    void mapsItemsAndReportsTotals() {
        var page = new PageImpl<>(List.of("a", "b"), PageRequest.of(0, 2), 5);

        PageResponseDto<String> response = PageResponseDto.of(page, String::toUpperCase);

        assertThat(response.items()).containsExactly("A", "B");
        assertThat(response.page()).isZero();
        assertThat(response.size()).isEqualTo(2);
        assertThat(response.totalItems()).isEqualTo(5);
        assertThat(response.totalPages()).isEqualTo(3);
    }

    @Test
    void emptyPageHasZeroTotalPages() {
        var page = new PageImpl<String>(List.of(), PageRequest.of(0, 20), 0);

        PageResponseDto<String> response = PageResponseDto.of(page, s -> s);

        assertThat(response.items()).isEmpty();
        assertThat(response.totalItems()).isZero();
        assertThat(response.totalPages()).isZero();
    }

    @Test
    void pageableCapsSizeAndSortsNewestFirst() {
        PageRequest request = PageResponseDto.pageable(2, 500);

        assertThat(request.getPageNumber()).isEqualTo(2);
        assertThat(request.getPageSize()).isEqualTo(PageResponseDto.MAX_SIZE);
        assertThat(request.getSort().getOrderFor("id")).isNotNull();
        assertThat(request.getSort().getOrderFor("id").getDirection()).isEqualTo(Sort.Direction.DESC);
    }
}