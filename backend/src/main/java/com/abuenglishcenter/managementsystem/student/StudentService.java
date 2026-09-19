package com.abuenglishcenter.managementsystem.student;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.abuenglishcenter.managementsystem.user.Role;
import com.abuenglishcenter.managementsystem.user.User;
import com.abuenglishcenter.managementsystem.user.UserRepository;

@Service 
public class StudentService {

    @Autowired 
    private StudentRepository studentRepository;

    @Autowired 
    private UserRepository userRepository;

    @Autowired 
    private PasswordEncoder passwordEncoder;

    public List<StudentResponseDto> getAllStudents() {
        return studentRepository.findAll().stream().map(this::toDto).toList();
    }

    public StudentResponseDto createStudent(StudentCreateRequestDto request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setRole(Role.STUDENT);
        User savedUser = userRepository.save(user);

        Student student = new Student();
        student.setUser(savedUser);
        student.setParentPhone(request.getParentPhone());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setStatus(Status.ACTIVE);
        Student savedStudent = studentRepository.save(student);

        return toDto(savedStudent);
    }

    private StudentResponseDto toDto(Student student) {
        return new StudentResponseDto(student.getId(),student.getUser().getUsername(),student.getUser().getFullName(), student.getParentPhone(), student.getDateOfBirth(), student.getStatus());
    }
}
