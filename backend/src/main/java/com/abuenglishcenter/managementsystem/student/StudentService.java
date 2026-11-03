package com.abuenglishcenter.managementsystem.student;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abuenglishcenter.managementsystem.exception.BusinessRuleException;
import com.abuenglishcenter.managementsystem.exception.NotFoundException;
import com.abuenglishcenter.managementsystem.user.Role;
import com.abuenglishcenter.managementsystem.user.User;
import com.abuenglishcenter.managementsystem.user.UserRepository;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(StudentRepository studentRepository, UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<StudentResponseDto> getAllStudents() {
        return studentRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional
    public StudentResponseDto createStudent(StudentCreateRequestDto request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessRuleException("Username '" + request.getUsername() + "' already exists.");
        }
        
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

    public StudentResponseDto getStudentById(Long id) {
        return toDto(findStudentOrThrow(id));
    }

    @Transactional
    public StudentResponseDto updateStudent(Long id, StudentUpdateRequestDto request) {
        Student checkStudent = findStudentOrThrow(id);

        // fullName lives on User; the entity is managed so the change flushes with the transaction
        checkStudent.getUser().setFullName(request.getFullName());
        checkStudent.setParentPhone(request.getParentPhone());
        checkStudent.setDateOfBirth(request.getDateOfBirth());
        checkStudent.setEvaluationSheetUrl(request.getEvaluationSheetUrl());

        return toDto(studentRepository.save(checkStudent));
    }

    @Transactional
    public StudentResponseDto updateStudentStatus(Long id, StudentStatus newStatus) {
        Student checkStudent = findStudentOrThrow(id);

        if (checkStudent.getStatus() == newStatus) {
            throw new BusinessRuleException("Student " + id + " is already " + newStatus + ".");
        }
        if (!checkStudent.getStatus().canMoveTo(newStatus)) {
            throw new BusinessRuleException("Cannot change student status from " + checkStudent.getStatus()
                    + " to " + newStatus + ".");
        }

        checkStudent.setStatus(newStatus);
        return toDto(studentRepository.save(checkStudent));
    }

    private Student findStudentOrThrow(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student with id " + id + " not found"));
    }

    private StudentResponseDto toDto(Student student) {
        return new StudentResponseDto(student.getId(), student.getUser().getUsername(), student.getUser().getFullName(),
                student.getParentPhone(), student.getDateOfBirth(), student.getStatus(),
                student.getEvaluationSheetUrl());
    }
}
