package com.abuenglishcenter.managementsystem.payroll;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abuenglishcenter.managementsystem.classroom.ClassSession;
import com.abuenglishcenter.managementsystem.classroom.ClassSessionRepository;
import com.abuenglishcenter.managementsystem.classroom.Classroom;
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

        LocalDate startDate = LocalDate.of(request.getYear(), request.getMonth(), 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        // Get all taught sessions in a month
        List<ClassSession> allSessions = classSessionRepository.findByTeacherTaughtTrueAndSessionDateBetween(startDate,
                endDate);

        // Filtering for spec teacher
        List<ClassSession> teacherSessions = allSessions.stream()
                .filter(s -> s.getClassroom().getTeacher().getId().equals(checkTeacher.getId())).toList();

        Map<Classroom, Long> sessionsPerClass = teacherSessions.stream()
                .collect(Collectors.groupingBy(ClassSession::getClassroom, Collectors.counting()));

        // Creating new a new payroll first
        TeacherPayroll newPayroll = new TeacherPayroll();
        newPayroll.setTeacher(checkTeacher);
        newPayroll.setMonth(request.getMonth());
        newPayroll.setYear(request.getYear());
        newPayroll.setBonus(BigDecimal.ZERO);
        newPayroll.setStatus(PayrollStatus.DRAFT);
        newPayroll.setManuallyAdjusted(false);

        BigDecimal sessionPay = BigDecimal.ZERO;
        List<PayrollDetail> details = new ArrayList<>();

        for (Map.Entry<Classroom, Long> entry : sessionsPerClass.entrySet()) {
            Classroom classroom = entry.getKey();
            int sessionsTaught = entry.getValue().intValue();

            // Find activating rate
            TeacherRate rate = teacherRateRepository
                    .findByTeacherIdAndClassroomIdAndEffectiveToIsNull(checkTeacher.getId(), classroom.getId())
                    .orElseThrow(() -> new RuntimeException("No active rate found for teacher " + checkTeacher.getId()
                            + " and classroom " + classroom.getId()));

            BigDecimal rateApplied = rate.getRatePerSession();
            BigDecimal subtotal = rateApplied.multiply(BigDecimal.valueOf(sessionsTaught));

            PayrollDetail newDetail = new PayrollDetail();
            newDetail.setPayroll(newPayroll);
            newDetail.setClassroom(classroom);
            newDetail.setSessionsTaught(sessionsTaught);
            newDetail.setRateApplied(rateApplied);
            newDetail.setSubtotal(subtotal);

            details.add(newDetail);
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
