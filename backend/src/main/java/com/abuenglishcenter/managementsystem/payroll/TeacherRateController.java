package com.abuenglishcenter.managementsystem.payroll;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping("/teacher-rates")
public class TeacherRateController {

    private final TeacherRateService teacherRateService;

    public TeacherRateController(TeacherRateService teacherRateService) {
        this.teacherRateService = teacherRateService;
    }
    
    @GetMapping 
    public List<TeacherRateResponseDto> getAllTeacherRates() {
        return teacherRateService.getAllTeacherRates();
    }

    @PostMapping 
    public TeacherRateResponseDto createTeacherRate(@Valid @RequestBody TeacherRateCreateRequestDto request) {
        return teacherRateService.createTeacherRate(request);
    }
}
