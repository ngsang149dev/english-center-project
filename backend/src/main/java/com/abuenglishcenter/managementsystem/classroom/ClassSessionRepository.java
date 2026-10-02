package com.abuenglishcenter.managementsystem.classroom;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassSessionRepository extends JpaRepository<ClassSession, Long> {
    List<ClassSession> findByTeacherIdAndTeacherTaughtTrueAndSessionDateBetween(Long teacherId,LocalDate start, LocalDate end);
}
