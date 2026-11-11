package com.abuenglishcenter.managementsystem.me;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abuenglishcenter.managementsystem.classroom.ClassSessionResponseDto;
import com.abuenglishcenter.managementsystem.classroom.ClassSessionService;
import com.abuenglishcenter.managementsystem.payroll.TeacherPayrollResponseDto;
import com.abuenglishcenter.managementsystem.payroll.TeacherPayrollService;
import com.abuenglishcenter.managementsystem.teacher.TeacherResponseDto;
import com.abuenglishcenter.managementsystem.teacher.TeacherService;

@RestController
@RequestMapping("/me/teacher")
public class MeTeacherController {

    private final TeacherService teacherService;
    private final TeacherPayrollService teacherPayrollService;
    private final ClassSessionService classSessionService;

    public MeTeacherController(TeacherService teacherService, TeacherPayrollService teacherPayrollService,
            ClassSessionService classSessionService) {
        this.teacherService = teacherService;
        this.teacherPayrollService = teacherPayrollService;
        this.classSessionService = classSessionService;
    }

    @GetMapping
    public TeacherResponseDto getProfile(@AuthenticationPrincipal Jwt jwt) {
        return teacherService.getTeacherByUserId(userId(jwt));
    }

    @GetMapping("/payrolls")
    public List<TeacherPayrollResponseDto> getPayrolls(@AuthenticationPrincipal Jwt jwt) {
        return teacherPayrollService.getPayrollsByTeacherId(teacherId(jwt));
    }

    @GetMapping("/sessions")
    public List<ClassSessionResponseDto> getSessions(@AuthenticationPrincipal Jwt jwt) {
        return classSessionService.getSessionsByTeacherId(teacherId(jwt));
    }

    private Long teacherId(Jwt jwt) {
        return teacherService.getTeacherByUserId(userId(jwt)).getId();
    }

    private Long userId(Jwt jwt) {
        return Long.valueOf(jwt.getSubject());
    }
}