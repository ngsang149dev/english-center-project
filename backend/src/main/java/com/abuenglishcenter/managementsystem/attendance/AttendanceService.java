package com.abuenglishcenter.managementsystem.attendance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abuenglishcenter.managementsystem.classroom.ClassSession;
import com.abuenglishcenter.managementsystem.classroom.ClassSessionRepository;
import com.abuenglishcenter.managementsystem.enrollment.Enrollment;
import com.abuenglishcenter.managementsystem.enrollment.EnrollmentRepository;
import com.abuenglishcenter.managementsystem.exception.BusinessRuleException;
import com.abuenglishcenter.managementsystem.exception.NotFoundException;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final ClassSessionRepository classSessionRepository;
    private final EnrollmentRepository enrollmentRepository;

    public AttendanceService(AttendanceRepository attendanceRepository, ClassSessionRepository classSessionRepository,
            EnrollmentRepository enrollmentRepository) {
        this.attendanceRepository = attendanceRepository;
        this.classSessionRepository = classSessionRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<AttendanceResponseDto> getAttendances(Long sessionId, Long enrollmentId) {
        List<Attendance> result;

        if (sessionId != null && enrollmentId != null) {
            result = attendanceRepository.findBySessionIdAndEnrollmentId(sessionId, enrollmentId);
        } else if (sessionId != null) {
            result = attendanceRepository.findBySessionId(sessionId);
        } else if (enrollmentId != null) {
            result = attendanceRepository.findByEnrollmentId(enrollmentId);
        } else {
            result = attendanceRepository.findAll();
        }
        
        return result.stream().map(this::toDto).toList();
    }

    @Transactional
    public List<AttendanceResponseDto> createAttendance(AttendanceCreateRequestDto request) {
        ClassSession checkSession = classSessionRepository.findById(request.getSessionId()).orElseThrow(
                () -> new NotFoundException("ClassSession with id " + request.getSessionId() + " not found"));
        List<Attendance> savedList = new ArrayList<>();

        Set<Long> seenEnrollmentIds = new HashSet<>();

        for (AttendanceItemDto item : request.getAttendances()) {

            Long enrollmentId = item.getEnrollmentId();
            if (!seenEnrollmentIds.add(enrollmentId)) {
                throw new BusinessRuleException(
                        "Enrollment " + enrollmentId + " appears more than once in the request.");
            }

            if (attendanceRepository.existsBySessionIdAndEnrollmentId(checkSession.getId(), enrollmentId)) {
                throw new BusinessRuleException("Attendance already exists for session " + checkSession.getId()
                        + " and enrollment " + enrollmentId + ".");
            }

            Enrollment checkEnrollment = enrollmentRepository.findById(item.getEnrollmentId()).orElseThrow(
                    () -> new NotFoundException("Enrollment with id " + item.getEnrollmentId() + " not found"));

            if (!checkEnrollment.getClassroom().getId().equals(checkSession.getClassroom().getId())) {
                throw new BusinessRuleException("Enrollment " + enrollmentId
                        + " does not belong to the class of session " + checkSession.getId() + ".");
            }

            Attendance newAttendance = new Attendance();
            newAttendance.setSession(checkSession);
            newAttendance.setEnrollment(checkEnrollment);
            newAttendance.setStatus(item.getStatus());

            Attendance savedAttendance = attendanceRepository.save(newAttendance);
            savedList.add(savedAttendance);
        }

        return savedList.stream().map(this::toDto).toList();
    }

    @Transactional 
    public AttendanceResponseDto updateAttendance(Long id, AttendanceUpdateRequestDto request) {
        Attendance checkAttendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Attendance with id " + id + " not found"));

        checkAttendance.setStatus(request.getStatus());
        Attendance saved = attendanceRepository.save(checkAttendance);
        return toDto(saved);
    }

    private AttendanceResponseDto toDto(Attendance attendance) {
        return new AttendanceResponseDto(attendance.getId(), attendance.getEnrollment().getId(),
                attendance.getSession().getId(), attendance.getStatus());
    }
}
