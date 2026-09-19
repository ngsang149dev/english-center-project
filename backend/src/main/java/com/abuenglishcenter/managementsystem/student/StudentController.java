package com.abuenglishcenter.managementsystem.student;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController 
@RequestMapping("/students")
public class StudentController {

    @Autowired 
    private StudentService studentService;

    @GetMapping
    public List<StudentResponseDto> getAllStudents() {
        return studentService.getAllStudents();
    }

    @PostMapping 
    public StudentResponseDto createStudent(@RequestBody StudentCreateRequestDto request) {
        return studentService.createStudent(request);
    }
    
}
