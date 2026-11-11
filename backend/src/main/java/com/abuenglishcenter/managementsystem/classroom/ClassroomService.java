package com.abuenglishcenter.managementsystem.classroom;

import org.springframework.stereotype.Service;

import com.abuenglishcenter.managementsystem.common.PageResponseDto;
import com.abuenglishcenter.managementsystem.exception.NotFoundException;
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

    public PageResponseDto<ClassroomResponseDto> getAllClassrooms(int page, int size) {
        return PageResponseDto.of(classroomRepository.findAll(PageResponseDto.pageable(page, size)), this::toDto);
    }

    public ClassroomResponseDto createClassroom(ClassroomCreateRequestDto request) {
        Teacher checkTeacher = teacherRepository.findById(request.getTeacherId()).orElseThrow(() -> new NotFoundException("Teacher with id " + request.getTeacherId() + " not found"));
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
        Classroom checkClassroom = classroomRepository.findById(classroomId).orElseThrow(() -> new NotFoundException("Classroom with id " + classroomId + " not found"));
        Teacher newTeacher = teacherRepository.findById(newTeacherId).orElseThrow(() -> new  RuntimeException("Teacher not found"));

        checkClassroom.setTeacher(newTeacher);
        Classroom updated = classroomRepository.save(checkClassroom);
        return toDto(updated);
    }
}
