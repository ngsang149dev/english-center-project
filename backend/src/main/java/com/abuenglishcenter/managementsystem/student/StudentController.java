package com.abuenglishcenter.managementsystem.student;

import java.util.List;

import jakarta.validation.Valid;
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
    public StudentResponseDto createStudent(@Valid @RequestBody StudentCreateRequestDto request) {
        return studentService.createStudent(request);
    }

    @GetMapping("/{id}")
    public StudentResponseDto getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @PutMapping("/{id}")
    public StudentResponseDto updateStudent(@PathVariable Long id, @Valid @RequestBody StudentUpdateRequestDto request) {
        return studentService.updateStudent(id, request);
    }

    @PutMapping("/{id}/status")
    public StudentResponseDto updateStudentStatus(@PathVariable Long id,
            @Valid @RequestBody StudentStatusUpdateRequestDto request) {
        return studentService.updateStudentStatus(id, request.getStatus());
    }
}
