package com.abuenglishcenter.managementsystem.student;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abuenglishcenter.managementsystem.user.Role;
import com.abuenglishcenter.managementsystem.user.User;
import com.abuenglishcenter.managementsystem.user.UserRepository;

@Service 
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(StudentRepository studentRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<StudentResponseDto> getAllStudents() {
        return studentRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional 
    public StudentResponseDto createStudent(StudentCreateRequestDto request) {
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setFullName(request.getFullName());
        newUser.setRole(Role.STUDENT);
        User savedUser = userRepository.save(newUser);

        Student newStudent = new Student();
        newStudent.setUser(savedUser);
        newStudent.setParentPhone(request.getParentPhone());
        newStudent.setDateOfBirth(request.getDateOfBirth());
        newStudent.setStatus(StudentStatus.ACTIVE);
        Student savedStudent = studentRepository.save(newStudent);

        return toDto(savedStudent);
    }

    private StudentResponseDto toDto(Student student) {
        return new StudentResponseDto(student.getId(),student.getUser().getUsername(),student.getUser().getFullName(), student.getParentPhone(), student.getDateOfBirth(), student.getStatus(), student.getEvaluationSheetUrl());
    }
}
