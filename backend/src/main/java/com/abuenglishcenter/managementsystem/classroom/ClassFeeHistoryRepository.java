package com.abuenglishcenter.managementsystem.classroom;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassFeeHistoryRepository extends JpaRepository<ClassFeeHistory, Long>{
    List<ClassFeeHistory> findByClassroomIdAndEffectiveToIsNull(Long classroomId);
}
