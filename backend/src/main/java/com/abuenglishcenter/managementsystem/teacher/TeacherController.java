package com.abuenglishcenter.managementsystem.teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController 
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired 
    private TeacherService teacherService;

    @GetMapping 
    public List<TeacherResponseDto> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @PostMapping
    public TeacherResponseDto createTeacher(@RequestBody TeacherCreateRequestDto request) {
        return teacherService.createTeacher(request);
    }

}

