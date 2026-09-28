package com.abuenglishcenter.managementsystem.payroll;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRateRepository extends JpaRepository<TeacherRate, Long> {
    Optional<TeacherRate> findByTeacherIdAndClassroomIdAndEffectiveToIsNull(Long teacherId, Long classroomId);
    List<TeacherRate> findByTeacherId(Long teacherId);
    List<TeacherRate> findByClassroomId(Long ClassroomId);
}
