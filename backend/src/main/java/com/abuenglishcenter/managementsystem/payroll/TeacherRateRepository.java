package com.abuenglishcenter.managementsystem.payroll;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeacherRateRepository extends JpaRepository<TeacherRate, Long> {
    Optional<TeacherRate> findByTeacherIdAndClassroomIdAndEffectiveToIsNull(Long teacherId, Long classroomId);

    List<TeacherRate> findByTeacherId(Long teacherId);

    List<TeacherRate> findByClassroomId(Long ClassroomId);

    @Query("SELECT r FROM TeacherRate r WHERE r.teacher.id = :teacherId AND r.classroom.id = :classroomId " +
            "AND r.effectiveFrom <= :date AND (r.effectiveTo IS NULL OR r.effectiveTo >= :date)")
    Optional<TeacherRate> findEffectiveRate(@Param("teacherId") Long teacherId,
            @Param("classroomId") Long classroomId, @Param("date") LocalDate date);
}
