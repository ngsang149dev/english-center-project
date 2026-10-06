package com.abuenglishcenter.managementsystem.payroll;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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

    private List<ClassSession> findTaughtSessions(Teacher teacher, int month, int year) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<ClassSession> teacherSessions = classSessionRepository
                .findByTeacherIdAndTeacherTaughtTrueAndSessionDateBetween(teacher.getId(), startDate, endDate);
        return teacherSessions;
    }

    private List<PayrollDetail> buildDetails(Teacher teacher, TeacherPayroll payroll, List<ClassSession> sessions) {
        Map<GroupKey, PayrollDetail> detailMap = new LinkedHashMap<>();

        for (ClassSession s : sessions) {
            Long classroomId = s.getClassroom().getId();

            TeacherRate rate = teacherRateRepository
                    .findEffectiveRate(teacher.getId(), classroomId, s.getSessionDate())
                    .orElseThrow(() -> new BusinessRuleException(
                            "There is no set salary level for " + teacher.getUser().getFullName()
                                    + " at class " + s.getClassroom().getName()
                                    + " in date " + s.getSessionDate()
                                    + ". Set up salary rates before calculating salaries."));

            GroupKey key = new GroupKey(classroomId, rate.getId());

            PayrollDetail newDetail = detailMap.computeIfAbsent(key, k -> {
                PayrollDetail d = new PayrollDetail();
                d.setPayroll(payroll);
                d.setClassroom(s.getClassroom());
                d.setRateApplied(rate.getRatePerSession());
                d.setSessionsTaught(0);
                return d;
            });

            newDetail.setSessionsTaught(newDetail.getSessionsTaught() + 1);
        }

        List<PayrollDetail> details = new ArrayList<>(detailMap.values());
        for (PayrollDetail d : details) {
            d.setSubtotal(d.getRateApplied().multiply(BigDecimal.valueOf(d.getSessionsTaught())));
        }
        return details;
    }

    private BigDecimal sumTotals(List<PayrollDetail> details) {
        return details.stream().map(PayrollDetail::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public TeacherPayrollResponseDto createTeacherPayroll(TeacherPayrollCreateRequestDto request) {
        Teacher checkTeacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        if (teacherPayrollRepository.existsByTeacherIdAndMonthAndYear(
                checkTeacher.getId(), request.getMonth(), request.getYear())) {
            throw new BusinessRuleException("Teacher " + checkTeacher.getUser().getFullName()
                    + " already has a payroll for " + request.getMonth() + "/" + request.getYear() + ".");
        }

        List<ClassSession> sessions = findTaughtSessions(checkTeacher, request.getMonth(), request.getYear());

        if (sessions.isEmpty()) {
            throw new BusinessRuleException("Teacher " + checkTeacher.getUser().getFullName()
                    + " has no taught sessions in " + request.getMonth() + "/" + request.getYear()
                    + ". Record the sessions before creating the payroll.");
        }

        // Create a new payroll
        TeacherPayroll newPayroll = new TeacherPayroll();
        newPayroll.setTeacher(checkTeacher);
        newPayroll.setMonth(request.getMonth());
        newPayroll.setYear(request.getYear());
        newPayroll.setBonus(BigDecimal.ZERO);
        newPayroll.setStatus(PayrollStatus.DRAFT);
        newPayroll.setManuallyAdjusted(false);

        List<PayrollDetail> details = buildDetails(checkTeacher, newPayroll, sessions);

        BigDecimal sessionPay = sumTotals(details);

        newPayroll.setSessionPay(sessionPay);
        newPayroll.setTotalPay(sessionPay);

        TeacherPayroll savedPayroll = teacherPayrollRepository.save(newPayroll);

        for (PayrollDetail detail : details) {
            detail.setPayroll(savedPayroll);
            payrollDetailRepository.save(detail);
        }

        return toDtoWithDetails(savedPayroll);
    }

    private void requireDraft(TeacherPayroll payroll, String action) {
        if (payroll.getStatus() != PayrollStatus.DRAFT) {
            throw new BusinessRuleException("Payroll is " + payroll.getStatus()
                    + " and cannot be " + action + ". Move it back to DRAFT first.");
        }
    }

    @Transactional
    public TeacherPayrollResponseDto recalculate(Long payrollId) {
        TeacherPayroll checkPayroll = teacherPayrollRepository.findById(payrollId)
                .orElseThrow(() -> new RuntimeException("Payroll not found"));

        requireDraft(checkPayroll, "recalculated");

        List<ClassSession> sessions = findTaughtSessions(checkPayroll.getTeacher(), checkPayroll.getMonth(),
                checkPayroll.getYear());

        List<PayrollDetail> newDetails = buildDetails(checkPayroll.getTeacher(), checkPayroll, sessions);
        payrollDetailRepository.deleteByPayrollId(payrollId);

        payrollDetailRepository.saveAll(newDetails);

        BigDecimal sessionPay = sumTotals(newDetails);
        checkPayroll.setSessionPay(sessionPay);
        checkPayroll.setTotalPay(sessionPay.add(checkPayroll.getBonus()));

        TeacherPayroll updated = teacherPayrollRepository.save(checkPayroll);
        return toDtoWithDetails(updated);
    }

    @Transactional
    public void deleteTeacherPayroll(Long id) {
        TeacherPayroll checkPayroll = teacherPayrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll not found"));
        requireDraft(checkPayroll, "deleted");
        payrollDetailRepository.deleteByPayrollId(id);
        teacherPayrollRepository.delete(checkPayroll);
    }

    @Transactional
    public TeacherPayrollResponseDto updateBonus(Long id, BigDecimal newBonus) {
        TeacherPayroll checkPayroll = teacherPayrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll not found"));

        requireDraft(checkPayroll, "given a bonus change");

        if (newBonus.signum() < 0) {
            throw new BusinessRuleException("Bonus must not be negative.");
        }
        
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

        if (!checkPayroll.getStatus().canMoveTo(newStatus)) {
            throw new BusinessRuleException("Cannot change payroll status from "
                    + checkPayroll.getStatus() + " to " + newStatus + ".");
        }

        if (checkPayroll.getStatus() == PayrollStatus.DRAFT && newStatus == PayrollStatus.CONFIRMED) {
            recalculate(id);
        }

        if (newStatus == PayrollStatus.PAID) {
            checkPayroll.setPaidDate(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        } else if (checkPayroll.getStatus() == PayrollStatus.PAID) {
            checkPayroll.setPaidDate(null);
        }
        
        checkPayroll.setStatus(newStatus);

        TeacherPayroll updatedStatus = teacherPayrollRepository.save(checkPayroll);
        return toDtoWithDetails(updatedStatus);
    }

    private record GroupKey(Long classroomId, Long rateId) {
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
                payroll.isManuallyAdjusted(), payroll.getStatus(), detailDtos, payroll.getPaidDate());
    }

}
