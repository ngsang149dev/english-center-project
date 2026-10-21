package com.abuenglishcenter.managementsystem.attendance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    boolean existsBySessionIdAndEnrollmentId(Long sessionId, Long enrollmentId);
    List<Attendance> findBySessionId(Long sessionId);
    List<Attendance> findByEnrollmentId(Long enrollmentId);
    List<Attendance> findBySessionIdAndEnrollmentId(Long sessionId, Long enrollmentId);
}
