package com.abuenglishcenter.managementsystem.attendance;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abuenglishcenter.managementsystem.classroom.ClassSession;
import com.abuenglishcenter.managementsystem.classroom.ClassSessionRepository;
import com.abuenglishcenter.managementsystem.enrollment.Enrollment;
import com.abuenglishcenter.managementsystem.enrollment.EnrollmentRepository;

@Service 
public class AttendanceService {
    @Autowired 
    private AttendanceRepository attendanceRepository;
    
    @Autowired 
    private ClassSessionRepository classSessionRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public List<AttendanceResponseDto> getAllAttendances() {
        return attendanceRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional 
    public List<AttendanceResponseDto> createAttendance(AttendanceCreateRequestDto request) {
        ClassSession checkSession = classSessionRepository.findById(request.getSessionId()).orElseThrow(() -> new RuntimeException("Class session not found"));

        List<Attendance> savedList = new ArrayList<>();

        for (AttendanceItemDto item: request.getAttendances()) {
            Enrollment checkEnrollment = enrollmentRepository.findById(item.getEnrollmentId()).orElseThrow(() -> new RuntimeException("Enrollment not found"));

            Attendance newAttendance = new Attendance();
            newAttendance.setSession(checkSession);
            newAttendance.setEnrollment(checkEnrollment);
            newAttendance.setStatus(item.getStatus());

            Attendance savedAttendance = attendanceRepository.save(newAttendance);
            savedList.add(savedAttendance);
        }
        
        return savedList.stream().map(this::toDto).toList();
    }

    private AttendanceResponseDto toDto(Attendance attendance) {
        return new AttendanceResponseDto(attendance.getId(), attendance.getEnrollment().getId(), attendance.getSession().getId(), attendance.getStatus());
    }
}
