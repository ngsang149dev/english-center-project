package com.abuenglishcenter.managementsystem.teacher;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController 
@RequestMapping("/teachers")
public class TeacherController {
    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping 
    public List<TeacherResponseDto> getAllTeachers() {
        return teacherService.getAllTeachers();
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
