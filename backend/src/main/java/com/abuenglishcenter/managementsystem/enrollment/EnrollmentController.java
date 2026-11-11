package com.abuenglishcenter.managementsystem.enrollment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abuenglishcenter.managementsystem.common.PageResponseDto;

@RestController
@RequestMapping("/enrollments") 
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping 
    public PageResponseDto<EnrollmentResponseDto> getAllEnrollments(@RequestParam(defaultValue = "0") @Min(0) @Max(1000) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return enrollmentService.getAllEnrollments(page, size);
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
    public EnrollmentResponseDto updateEnrollment(@PathVariable Long id, @Valid @RequestBody EnrollmentUpdateRequestDto request) {
        return enrollmentService.updateEnrollment(id, request.getEnrolledDate());
    }
    
    @PutMapping("/{id}/status")
    public EnrollmentResponseDto updateEnrollmentStatus(@PathVariable Long id, @Valid @RequestBody EnrollmentStatusUpdateRequestDto request) {
        return enrollmentService.updateEnrollmentStatus(id, request.getStatus());
    }


}
