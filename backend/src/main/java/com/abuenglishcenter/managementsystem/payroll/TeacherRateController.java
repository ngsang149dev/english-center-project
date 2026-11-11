package com.abuenglishcenter.managementsystem.payroll;

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
@RequestMapping("/teacher-rates")
public class TeacherRateController {

    private final TeacherRateService teacherRateService;

    public TeacherRateController(TeacherRateService teacherRateService) {
        this.teacherRateService = teacherRateService;
    }
    
    @GetMapping 
    public PageResponseDto<TeacherRateResponseDto> getAllTeacherRates(@RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return teacherRateService.getAllTeacherRates(page, size);
    }

    @PostMapping 
    public TeacherRateResponseDto createTeacherRate(@Valid @RequestBody TeacherRateCreateRequestDto request) {
        return teacherRateService.createTeacherRate(request);
    }
}
