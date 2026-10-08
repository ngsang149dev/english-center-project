package com.abuenglishcenter.managementsystem.classroom;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ClassScheduleService {

    private final ClassScheduleRepository classScheduleRepository;
    private final ClassroomRepository classroomRepository;

    public ClassScheduleService(ClassScheduleRepository classScheduleRepository,
            ClassroomRepository classroomRepository) {
        this.classScheduleRepository = classScheduleRepository;
        this.classroomRepository = classroomRepository;
    }

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
