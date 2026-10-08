package com.abuenglishcenter.managementsystem.student;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController 
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<StudentResponseDto> getAllStudents() {
        return studentService.getAllStudents();
    }

    @PostMapping 
    public StudentResponseDto createStudent(@RequestBody StudentCreateRequestDto request) {
        return studentService.createStudent(request);
    }
    
}
