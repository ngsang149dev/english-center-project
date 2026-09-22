package com.abuenglishcenter.managementsystem.classroom;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service 
public class ClassScheduleService {

    @Autowired 
    private ClassScheduleRepository classScheduleRepository;

    @Autowired 
    private ClassroomRepository classroomRepository;

    public List<ClassScheduleResponseDto> getAllClassSchedules() {
        return classScheduleRepository.findAll().stream().map(this::toDto).toList();
    }

    public List<ClassScheduleResponseDto> getSchedulesByClassroom(Long classroomId) {
        return classScheduleRepository.findByClassroomId(classroomId).stream().map(this::toDto).toList();
    }

    public ClassScheduleResponseDto createSchedule(ClassScheduleCreateRequestDto request) {
        Classroom checkClassroom = classroomRepository.findById(request.getClassId()).orElseThrow(() -> new RuntimeException("Classroom not found"));

        ClassSchedule newSchedule = new ClassSchedule();
        newSchedule.setClassroom(checkClassroom);
        newSchedule.setDayOfWeek(request.getDayOfWeek());
        newSchedule.setStartTime(request.getStartTime());

        ClassSchedule saved = classScheduleRepository.save(newSchedule);
        return toDto(saved);
    }

    private ClassScheduleResponseDto toDto(ClassSchedule classSchedule) {
        return new ClassScheduleResponseDto(classSchedule.getId(), classSchedule.getClassroom().getId(), classSchedule.getDayOfWeek(), classSchedule.getStartTime());
    }

}
