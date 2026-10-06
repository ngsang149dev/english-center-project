package com.abuenglishcenter.managementsystem.classroom;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClassFeeHistoryRepository extends JpaRepository<ClassFeeHistory, Long> {
    List<ClassFeeHistory> findByClassroomIdAndEffectiveToIsNull(Long classroomId);

    @Query("SELECT c FROM ClassFeeHistory c WHERE c.classroom.id = :classroomId " +
            "AND c.effectiveFrom <= :date AND (c.effectiveTo IS NULL OR c.effectiveTo >= :date)")
    Optional<ClassFeeHistory> findEffectiveFee(@Param("classroomId") Long classroomId, @Param("date") LocalDate date);
}
