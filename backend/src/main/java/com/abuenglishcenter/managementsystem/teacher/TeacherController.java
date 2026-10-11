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

}
