package com.abuenglishcenter.managementsystem.enrollment;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long>{
    List<Enrollment> findByStudentIdAndStatus(Long studentId, EnrollmentStatus status);
    List<Enrollment> findByStatusAndEnrolledDateLessThanEqual(EnrollmentStatus status, LocalDate date);
}
