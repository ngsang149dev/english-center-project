package com.abuenglishcenter.managementsystem.student;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.abuenglishcenter.managementsystem.exception.BusinessRuleException;
import com.abuenglishcenter.managementsystem.user.Role;
import com.abuenglishcenter.managementsystem.user.User;
import com.abuenglishcenter.managementsystem.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentService = new StudentService(studentRepository, userRepository, passwordEncoder);
    }

    @Test
    void settingTheSameStatusIsAConflict() {
        givenStudent(StudentStatus.ACTIVE);

        assertThatThrownBy(() -> studentService.updateStudentStatus(1L, StudentStatus.ACTIVE))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Student 1 is already ACTIVE.");

        verify(studentRepository, never()).save(any());
    }

    @Test
    void alumnusCannotBecomeActiveAgain() {
        givenStudent(StudentStatus.ALUMNI);

        assertThatThrownBy(() -> studentService.updateStudentStatus(1L, StudentStatus.ACTIVE))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Cannot change student status from ALUMNI to ACTIVE.");

        verify(studentRepository, never()).save(any());
    }

    @Test
    void activeStudentCanGraduate() {
        Student student = givenStudent(StudentStatus.ACTIVE);
        when(studentRepository.save(any(Student.class))).thenAnswer(call -> call.getArgument(0));

        StudentResponseDto response = studentService.updateStudentStatus(1L, StudentStatus.ALUMNI);

        assertThat(response.getStatus()).isEqualTo(StudentStatus.ALUMNI);
        assertThat(student.getStatus()).isEqualTo(StudentStatus.ALUMNI);
    }

    private Student givenStudent(StudentStatus status) {
        User user = new User();
        user.setId(5L);
        user.setUsername("student1");
        user.setFullName("Student One");
        user.setRole(Role.STUDENT);

        Student student = new Student();
        student.setId(1L);
        student.setUser(user);
        student.setStatus(status);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        return student;
    }
}