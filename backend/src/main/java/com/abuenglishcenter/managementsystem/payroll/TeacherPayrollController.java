package com.abuenglishcenter.managementsystem.payroll;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping("/teacher-payrolls")
public class TeacherPayrollController {
    @Autowired 
    private TeacherPayrollService teacherPayrollService;

    @GetMapping 
    public List<TeacherPayrollResponseDto> getAllTeacherPayrolls() {
        return teacherPayrollService.getAllTeacherPayrolls();
    }

    @PostMapping 
    public TeacherPayrollResponseDto createTeacherPayroll(@RequestBody TeacherPayrollCreateRequestDto request) {
        return teacherPayrollService.createTeacherPayroll(request);
    } 

    @PutMapping("/{id}/bonus")
    public TeacherPayrollResponseDto updateBonus(@PathVariable Long id, @RequestBody BigDecimal newBonus) {
        return teacherPayrollService.updateBonus(id, newBonus);
    }

    @PutMapping("/{id}/status")
    public TeacherPayrollResponseDto updateStatus(@PathVariable Long id, @RequestBody PayrollStatus newStatus) {
        return teacherPayrollService.updateStatus(id, newStatus);
    }
}
