package com.abuenglishcenter.managementsystem.teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abuenglishcenter.managementsystem.user.Role;
import com.abuenglishcenter.managementsystem.user.User;
import com.abuenglishcenter.managementsystem.user.UserRepository;

import java.util.List;

@Service 
public class TeacherService {

    @Autowired 
    private UserRepository userRepository;

    @Autowired 
    private TeacherRepository teacherRepository;

    public List<TeacherResponseDto> getAllTeachers() {
        return teacherRepository.findAll().stream().map(this::toDto).toList();
    }

    public TeacherResponseDto createTeacher(TeacherCreateRequestDto request) {
        // TODO user builder pattern
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFullName(request.getFullName());
        user.setRole(Role.TEACHER);
        User savedUser = userRepository.save(user);

        Teacher teacher = new Teacher();
        teacher.setUser(savedUser);
        teacher.setSpecialization(request.getSpecialization());
        teacher.setPhone(request.getPhone());
        Teacher savedTeacher = teacherRepository.save(teacher);

        return toDto(savedTeacher);
    }

    private TeacherResponseDto toDto(Teacher teacher) {
        return new TeacherResponseDto(teacher.getId(),teacher.getUser().getUsername(),teacher.getUser().getFullName(), teacher.getSpecialization(), teacher.getPhone());
    }
}
