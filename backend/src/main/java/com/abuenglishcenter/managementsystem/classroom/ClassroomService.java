package com.abuenglishcenter.managementsystem.classroom;

import java.util.List;

import org.springframework.stereotype.Service;

import com.abuenglishcenter.managementsystem.teacher.Teacher;
import com.abuenglishcenter.managementsystem.teacher.TeacherRepository;

@Service
public class ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final TeacherRepository teacherRepository;

    public ClassroomService(ClassroomRepository classroomRepository, TeacherRepository teacherRepository) {
        this.classroomRepository = classroomRepository;
        this.teacherRepository = teacherRepository;
    }

    public List<ClassroomResponseDto> getAllClassrooms() {
        return classroomRepository.findAll().stream().map(this::toDto).toList();
    }

    public ClassroomResponseDto createClassroom(ClassroomCreateRequestDto request) {
        Teacher checkTeacher = teacherRepository.findById(request.getTeacherId()).orElseThrow(() -> new RuntimeException("Teacher not found"));
        Classroom newClassroom = new Classroom();
        newClassroom.setName(request.getName());
        newClassroom.setTeacher(checkTeacher);

        Classroom saved = classroomRepository.save(newClassroom);
        return toDto(saved);
    }

    private ClassroomResponseDto toDto(Classroom classroom) {
        return new ClassroomResponseDto(classroom.getId(), classroom.getName(), classroom.getTeacher().getId(), classroom.getCurriculumSheetUrl());
    }

    public ClassroomResponseDto updateTeacher(Long classroomId, Long newTeacherId) {
        Classroom checkClassroom = classroomRepository.findById(classroomId).orElseThrow(() -> new RuntimeException("Classroom not found"));
        Teacher newTeacher = teacherRepository.findById(newTeacherId).orElseThrow(() -> new  RuntimeException("Teacher not found"));

        checkClassroom.setTeacher(newTeacher);
        Classroom updated = classroomRepository.save(checkClassroom);
        return toDto(updated);
    }
}
