package com.abuenglishcenter.managementsystem.classroom;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abuenglishcenter.managementsystem.teacher.Teacher;
import com.abuenglishcenter.managementsystem.teacher.TeacherRepository;

@Service 
public class ClassroomService {

    @Autowired 
    private ClassroomRepository classroomRepository;

    @Autowired 
    private TeacherRepository teacherRepository;

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
        Classroom classroom = classroomRepository.findById(classroomId).orElseThrow(() -> new RuntimeException("Classroom not found"));
        Teacher newTeacher = teacherRepository.findById(newTeacherId).orElseThrow(() -> new  RuntimeException("Teacher not found"));

        classroom.setTeacher(newTeacher);
        Classroom updated = classroomRepository.save(classroom);
        return toDto(updated);
    }
}
