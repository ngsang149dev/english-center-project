package com.abuenglishcenter.managementsystem.student;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import org.springframework.web.bind.annotation.*;

import com.abuenglishcenter.managementsystem.common.PageResponseDto;

@RestController 
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public PageResponseDto<StudentResponseDto> getAllStudents(@RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return studentService.getAllStudents(page, size);
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
