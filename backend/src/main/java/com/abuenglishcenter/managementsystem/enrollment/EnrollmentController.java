package com.abuenglishcenter.managementsystem.enrollment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enrollments") 
public class EnrollmentController {

    @Autowired 
    private EnrollmentService enrollmentService;

    @GetMapping 
    public List<EnrollmentResponseDto> getAllEnrollments() {
        return enrollmentService.getAllEnrollments();
    }

    @PostMapping 
    public EnrollmentResponseDto createEnrollment(@RequestBody EnrollmentCreateRequestDto request) {
        return enrollmentService.createEnrollment(request);
    }
}
