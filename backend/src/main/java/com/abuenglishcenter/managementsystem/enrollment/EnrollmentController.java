package com.abuenglishcenter.managementsystem.enrollment;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enrollments") 
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping 
    public List<EnrollmentResponseDto> getAllEnrollments() {
        return enrollmentService.getAllEnrollments();
    }

    @PostMapping 
    public EnrollmentResponseDto createEnrollment(@Valid @RequestBody EnrollmentCreateRequestDto request) {
        return enrollmentService.createEnrollment(request);
    }

    @GetMapping("/{id}")
    public EnrollmentResponseDto getEnrollmentById(@PathVariable Long id) {
        return enrollmentService.getEnrollmentById(id);
    }

    @PutMapping("/{id}")
    public EnrollmentResponseDto updateEnrollment(@PathVariable Long id, @RequestBody EnrollmentUpdateRequestDto request) {
        return enrollmentService.updateEnrollment(id, request.getEnrolledDate());
    }
    
    @PutMapping("/{id}/status")
    public EnrollmentResponseDto updateEnrollmentStatus(@PathVariable Long id, @RequestBody EnrollmentStatusUpdateRequestDto request) {
        return enrollmentService.updateEnrollmentStatus(id, request.getStatus());
    }


}
