package com.abuenglishcenter.managementsystem.classroom;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/class-fee-history")
public class ClassFeeHistoryController {

    private final ClassFeeHistoryService classFeeHistoryService;

    public ClassFeeHistoryController(ClassFeeHistoryService classFeeHistoryService) {
        this.classFeeHistoryService = classFeeHistoryService;
    }

    @GetMapping
    public List<ClassFeeHistoryResponseDto> getAllHistoryFees() {
        return classFeeHistoryService.getAllHistoryFees();
    }

    @PostMapping
    public ClassFeeHistoryResponseDto createFeeHistory(@Valid @RequestBody ClassFeeHistoryCreateRequestDto request) {
        return classFeeHistoryService.createFeeHistory(request);
    }
}
