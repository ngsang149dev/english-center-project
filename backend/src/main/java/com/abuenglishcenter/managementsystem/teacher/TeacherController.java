package com.abuenglishcenter.managementsystem.teacher;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import org.springframework.web.bind.annotation.*;

import com.abuenglishcenter.managementsystem.common.PageResponseDto;

@RestController 
@RequestMapping("/teachers")
public class TeacherController {
    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping 
    public PageResponseDto<TeacherResponseDto> getAllTeachers(@RequestParam(defaultValue = "0") @Min(0) @Max(1000) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return teacherService.getAllTeachers(page, size);
    }

    @PostMapping
    public TeacherResponseDto createTeacher(@Valid @RequestBody TeacherCreateRequestDto request) {
        return teacherService.createTeacher(request);
    }

    @GetMapping("/{id}")
    public TeacherResponseDto getTeacherById(@PathVariable Long id) {
        return teacherService.getTeacherById(id);
    }

    @PutMapping("/{id}")
    public TeacherResponseDto updateTeacher(@PathVariable Long id, @Valid @RequestBody TeacherUpdateRequestDto request) {
        return teacherService.updateTeacher(id, request);
    }
}
