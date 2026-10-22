package com.abuenglishcenter.managementsystem.enrollment;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long>{
    List<Enrollment> findByStatusAndEnrolledDateLessThanEqual(EnrollmentStatus status, LocalDate date);
    boolean existsByStudentIdAndClassroomIdAndStatus(Long studentId, Long classroomId, EnrollmentStatus status);
}
