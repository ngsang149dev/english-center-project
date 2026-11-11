package com.abuenglishcenter.managementsystem.attendance;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    boolean existsBySessionIdAndEnrollmentId(Long sessionId, Long enrollmentId);
    Page<Attendance> findBySessionId(Long sessionId, Pageable pageable);
    Page<Attendance> findByEnrollmentId(Long enrollmentId, Pageable pageable);
    Page<Attendance> findBySessionIdAndEnrollmentId(Long sessionId, Long enrollmentId, Pageable pageable);
    List<Attendance> findBySessionId(Long sessionId);
    List<Attendance> findByEnrollmentId(Long enrollmentId);
    List<Attendance> findBySessionIdAndEnrollmentId(Long sessionId, Long enrollmentId);
    List<Attendance> findByEnrollmentStudentId(Long studentId);
}
