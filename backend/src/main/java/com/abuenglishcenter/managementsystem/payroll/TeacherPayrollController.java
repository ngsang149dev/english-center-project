package com.abuenglishcenter.managementsystem.payroll;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping("/teacher-payrolls")
public class TeacherPayrollController {

    private final TeacherPayrollService teacherPayrollService;

    public TeacherPayrollController(TeacherPayrollService teacherPayrollService) {
        this.teacherPayrollService = teacherPayrollService;
    }

    @GetMapping 
    public List<TeacherPayrollResponseDto> getAllTeacherPayrolls() {
        return teacherPayrollService.getAllTeacherPayrolls();
    }

    @PostMapping 
    public TeacherPayrollResponseDto createTeacherPayroll(@Valid @RequestBody TeacherPayrollCreateRequestDto request) {
        return teacherPayrollService.createTeacherPayroll(request);
    } 

    @PutMapping("/{id}/bonus")
    public TeacherPayrollResponseDto updateBonus(@PathVariable Long id,
            @Valid @RequestBody TeacherPayrollBonusUpdateRequestDto request) {
        return teacherPayrollService.updateBonus(id, request.getBonus());
    }

    @PutMapping("/{id}/status")
    public TeacherPayrollResponseDto updateStatus(@PathVariable Long id,
            @Valid @RequestBody TeacherPayrollStatusUpdateRequestDto request) {
        return teacherPayrollService.updateStatus(id, request.getStatus());
    }

    @PostMapping("/{id}/recalculate")
    public TeacherPayrollResponseDto recalculate(@PathVariable Long id) {
        return teacherPayrollService.recalculate(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeacherPayroll(@PathVariable Long id) {
        teacherPayrollService.deleteTeacherPayroll(id);

    }
}
