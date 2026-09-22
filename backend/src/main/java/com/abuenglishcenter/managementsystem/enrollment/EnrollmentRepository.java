package com.abuenglishcenter.managementsystem.enrollment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long>{
    List<Enrollment> findByStudentIdAndStatus(Long studentId, Status status);
}
