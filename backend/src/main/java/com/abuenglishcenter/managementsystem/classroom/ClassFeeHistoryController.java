package com.abuenglishcenter.managementsystem.classroom;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abuenglishcenter.managementsystem.common.PageResponseDto;

@RestController
@RequestMapping("/class-fee-history")
public class ClassFeeHistoryController {

    private final ClassFeeHistoryService classFeeHistoryService;

    public ClassFeeHistoryController(ClassFeeHistoryService classFeeHistoryService) {
        this.classFeeHistoryService = classFeeHistoryService;
    }

    @GetMapping
    public PageResponseDto<ClassFeeHistoryResponseDto> getAllHistoryFees(@RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return classFeeHistoryService.getAllHistoryFees(page, size);
    }

    @PostMapping
    public ClassFeeHistoryResponseDto createFeeHistory(@Valid @RequestBody ClassFeeHistoryCreateRequestDto request) {
        return classFeeHistoryService.createFeeHistory(request);
    }
}
