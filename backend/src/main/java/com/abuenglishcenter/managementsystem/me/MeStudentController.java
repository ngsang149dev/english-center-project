package com.abuenglishcenter.managementsystem.me;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abuenglishcenter.managementsystem.attendance.AttendanceResponseDto;
import com.abuenglishcenter.managementsystem.attendance.AttendanceService;
import com.abuenglishcenter.managementsystem.billing.InvoiceResponseDto;
import com.abuenglishcenter.managementsystem.billing.InvoiceService;
import com.abuenglishcenter.managementsystem.enrollment.EnrollmentResponseDto;
import com.abuenglishcenter.managementsystem.enrollment.EnrollmentService;
import com.abuenglishcenter.managementsystem.student.StudentResponseDto;
import com.abuenglishcenter.managementsystem.student.StudentService;

@RestController
@RequestMapping("/me/student")
public class MeStudentController {

    private final StudentService studentService;
    private final EnrollmentService enrollmentService;
    private final InvoiceService invoiceService;
    private final AttendanceService attendanceService;

    public MeStudentController(StudentService studentService, EnrollmentService enrollmentService,
            InvoiceService invoiceService, AttendanceService attendanceService) {
        this.studentService = studentService;
        this.enrollmentService = enrollmentService;
        this.invoiceService = invoiceService;
        this.attendanceService = attendanceService;
    }

    @GetMapping
    public StudentResponseDto getProfile(@AuthenticationPrincipal Jwt jwt) {
        return studentService.getStudentByUserId(userId(jwt));
    }

    @GetMapping("/enrollments")
    public List<EnrollmentResponseDto> getEnrollments(@AuthenticationPrincipal Jwt jwt) {
        return enrollmentService.getEnrollmentsByStudentId(studentId(jwt));
    }

    @GetMapping("/invoices")
    public List<InvoiceResponseDto> getInvoices(@AuthenticationPrincipal Jwt jwt) {
        return invoiceService.getInvoicesByStudent(studentId(jwt));
    }

    @GetMapping("/attendances")
    public List<AttendanceResponseDto> getAttendances(@AuthenticationPrincipal Jwt jwt) {
        return attendanceService.getAttendancesByStudentId(studentId(jwt));
    }

    private Long studentId(Jwt jwt) {
        return studentService.getStudentByUserId(userId(jwt)).getId();
    }

    private Long userId(Jwt jwt) {
        return Long.valueOf(jwt.getSubject());
    }
}