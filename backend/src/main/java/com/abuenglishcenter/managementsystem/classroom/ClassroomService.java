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
        Teacher teacher = teacherRepository.findById(request.getTeacherId()).orElseThrow(() -> new RuntimeException("Teacher not found"));
        Classroom classroom = new Classroom();
        classroom.setName(request.getName());
        classroom.setTeacher(teacher);

        Classroom saved = classroomRepository.save(classroom);
        return toDto(saved);
    }

    private ClassroomResponseDto toDto(Classroom classroom) {
        return new ClassroomResponseDto(classroom.getId(), classroom.getName(), classroom.getTeacher().getId(), classroom.getCurriculumSheetUrl());
    }
}
