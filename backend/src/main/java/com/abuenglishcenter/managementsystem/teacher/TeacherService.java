package com.abuenglishcenter.managementsystem.teacher;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abuenglishcenter.managementsystem.user.Role;
import com.abuenglishcenter.managementsystem.user.User;
import com.abuenglishcenter.managementsystem.user.UserRepository;

import java.util.List;

@Service 
public class TeacherService {

    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;

    public TeacherService(UserRepository userRepository, TeacherRepository teacherRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<TeacherResponseDto> getAllTeachers() {
        return teacherRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional 
    public TeacherResponseDto createTeacher(TeacherCreateRequestDto request) {
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setFullName(request.getFullName());
        newUser.setRole(Role.TEACHER);
        User savedUser = userRepository.save(newUser);

        Teacher newTeacher = new Teacher();
        newTeacher.setUser(savedUser);
        newTeacher.setSpecialization(request.getSpecialization());
        newTeacher.setPhone(request.getPhone());
        Teacher savedTeacher = teacherRepository.save(newTeacher);

        return toDto(savedTeacher);
    }

    private TeacherResponseDto toDto(Teacher teacher) {
        return new TeacherResponseDto(teacher.getId(),teacher.getUser().getUsername(),teacher.getUser().getFullName(), teacher.getSpecialization(), teacher.getPhone());
    }
}
