package com.abuenglishcenter.managementsystem.payroll;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abuenglishcenter.managementsystem.classroom.ClassSession;
import com.abuenglishcenter.managementsystem.classroom.ClassSessionRepository;
import com.abuenglishcenter.managementsystem.exception.BusinessRuleException;
import com.abuenglishcenter.managementsystem.teacher.Teacher;
import com.abuenglishcenter.managementsystem.teacher.TeacherRepository;

@Service
public class TeacherPayrollService {
    @Autowired
    private TeacherPayrollRepository teacherPayrollRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ClassSessionRepository classSessionRepository;

    @Autowired
    private TeacherRateRepository teacherRateRepository;

    @Autowired
    private PayrollDetailRepository payrollDetailRepository;

    public List<TeacherPayrollResponseDto> getAllTeacherPayrolls() {
        return teacherPayrollRepository.findAll().stream().map(this::toDtoWithDetails).toList();
    }

    @Transactional
    public TeacherPayrollResponseDto createTeacherPayroll(TeacherPayrollCreateRequestDto request) {
        Teacher checkTeacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        if (teacherPayrollRepository.existsByTeacherIdAndMonthAndYear(
                checkTeacher.getId(), request.getMonth(), request.getYear())) {
            throw new BusinessRuleException("Teacher " + checkTeacher.getUser().getFullName()
                    + " has already have monthly payroll " + request.getMonth() + "/" + request.getYear() + ".");
        }

        LocalDate startDate = LocalDate.of(request.getYear(), request.getMonth(), 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<ClassSession> teacherSessions = classSessionRepository
                .findByTeacherIdAndTeacherTaughtTrueAndSessionDateBetween(checkTeacher.getId(), startDate, endDate);

        // Creating new a new payroll first
        TeacherPayroll newPayroll = new TeacherPayroll();
        newPayroll.setTeacher(checkTeacher);
        newPayroll.setMonth(request.getMonth());
        newPayroll.setYear(request.getYear());
        newPayroll.setBonus(BigDecimal.ZERO);
        newPayroll.setStatus(PayrollStatus.DRAFT);
        newPayroll.setManuallyAdjusted(false);

        Map<GroupKey, PayrollDetail> detailMap = new LinkedHashMap<>();

        for (ClassSession s : teacherSessions) {
            Long classroomId = s.getClassroom().getId();

            TeacherRate rate = teacherRateRepository
                    .findEffectiveRate(checkTeacher.getId(), classroomId, s.getSessionDate())
                    .orElseThrow(() -> new BusinessRuleException(
                            "There is no set salary level for " + checkTeacher.getUser().getFullName()
                                    + " at class " + s.getClassroom().getName()
                                    + " in date " + s.getSessionDate()
                                    + ". Set up salary rates before calculating salaries."));

            GroupKey key = new GroupKey(classroomId, rate.getId());

            PayrollDetail newDetail = detailMap.computeIfAbsent(key, k -> {
                PayrollDetail d = new PayrollDetail();
                d.setPayroll(newPayroll);
                d.setClassroom(s.getClassroom());
                d.setRateApplied(rate.getRatePerSession());
                d.setSessionsTaught(0);
                return d;
            });

            newDetail.setSessionsTaught(newDetail.getSessionsTaught() + 1);
        }

        BigDecimal sessionPay = BigDecimal.ZERO;
        List<PayrollDetail> details = new ArrayList<>(detailMap.values());

        for (PayrollDetail d : details) {
            BigDecimal subtotal = d.getRateApplied().multiply(BigDecimal.valueOf(d.getSessionsTaught()));
            d.setSubtotal(subtotal);
            sessionPay = sessionPay.add(subtotal);
        }

        newPayroll.setSessionPay(sessionPay);
        newPayroll.setTotalPay(sessionPay);

        TeacherPayroll savedPayroll = teacherPayrollRepository.save(newPayroll);

        for (PayrollDetail detail : details) {
            detail.setPayroll(savedPayroll);
            payrollDetailRepository.save(detail);
        }

        return toDtoWithDetails(savedPayroll);
    }

    @Transactional
    public TeacherPayrollResponseDto updateBonus(Long id, BigDecimal newBonus) {
        TeacherPayroll checkPayroll = teacherPayrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll not found"));

        checkPayroll.setBonus(newBonus);
        checkPayroll.setManuallyAdjusted(true);
        checkPayroll.setTotalPay(newBonus.add(checkPayroll.getSessionPay()));
        TeacherPayroll updatedBonus = teacherPayrollRepository.save(checkPayroll);
        return toDtoWithDetails(updatedBonus);
    }

    @Transactional
    public TeacherPayrollResponseDto updateStatus(Long id, PayrollStatus newStatus) {
        TeacherPayroll checkPayroll = teacherPayrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll not found"));
        checkPayroll.setStatus(newStatus);

        TeacherPayroll updatedStatus = teacherPayrollRepository.save(checkPayroll);
        return toDtoWithDetails(updatedStatus);
    }

    public record GroupKey(Long classroomId, Long rateId) {
    }

    private TeacherPayrollResponseDto toDtoWithDetails(TeacherPayroll payroll) {
        List<PayrollDetail> details = payrollDetailRepository.findByPayrollId(payroll.getId());
        return toDto(payroll, details);
    }

    private PayrollDetailResponseDto toDetailDto(PayrollDetail payrollDetail) {
        return new PayrollDetailResponseDto(payrollDetail.getId(), payrollDetail.getPayroll().getId(),
                payrollDetail.getClassroom().getId(), payrollDetail.getSessionsTaught(), payrollDetail.getRateApplied(),
                payrollDetail.getSubtotal());
    }

    private TeacherPayrollResponseDto toDto(TeacherPayroll payroll, List<PayrollDetail> details) {
        List<PayrollDetailResponseDto> detailDtos = details.stream().map(this::toDetailDto).toList();
        return new TeacherPayrollResponseDto(payroll.getId(), payroll.getTeacher().getId(), payroll.getMonth(),
                payroll.getYear(), payroll.getSessionPay(), payroll.getBonus(), payroll.getTotalPay(),
                payroll.isManuallyAdjusted(), payroll.getStatus(), detailDtos);
    }
}
