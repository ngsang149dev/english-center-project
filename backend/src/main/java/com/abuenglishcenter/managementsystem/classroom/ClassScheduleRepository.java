package com.abuenglishcenter.managementsystem.classroom;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassScheduleRepository extends JpaRepository<ClassSchedule, Long> {
    List<ClassSchedule> findByClassroomId(Long classroomId);
}
