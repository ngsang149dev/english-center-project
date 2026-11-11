package com.abuenglishcenter.managementsystem.auth;

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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import com.abuenglishcenter.managementsystem.exception.ForbiddenException;
import com.abuenglishcenter.managementsystem.student.Student;
import com.abuenglishcenter.managementsystem.student.StudentRepository;
import com.abuenglishcenter.managementsystem.student.StudentStatus;
import com.abuenglishcenter.managementsystem.user.Role;
import com.abuenglishcenter.managementsystem.user.User;
import com.abuenglishcenter.managementsystem.user.UserRepository;
import com.abuenglishcenter.managementsystem.user.UserResponseDto;
import com.abuenglishcenter.managementsystem.user.UserService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private static final String RAW_PASSWORD = "password123";

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private UserService userService;

    @Mock
    private StudentRepository studentRepository;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository, passwordEncoder, jwtEncoder, 480, userService, studentRepository);
    }

    @Test
    void activeStudentReceivesToken() {
        User user = studentUser(3L);
        givenValidCredentials(user, RAW_PASSWORD);
        givenStudentStatus(3L, StudentStatus.ACTIVE);
        when(jwtEncoder.encode(any())).thenReturn(token("jwt-token"));
        when(userService.getUserById(3L)).thenReturn(new UserResponseDto(3L, "student1", "STUDENT", "Student One"));

        LoginResponseDto response = authService.login(loginRequest("student1", RAW_PASSWORD));

        assertThat(response.getAccessToken()).isEqualTo("jwt-token");
        assertThat(response.getTokenType()).isEqualTo("Bearer");
        assertThat(response.getExpiresIn()).isEqualTo(480 * 60);
        assertThat(response.getUser().getUsername()).isEqualTo("student1");
    }

    @Test
    void inactiveStudentIsForbiddenAndGetsNoToken() {
        User user = studentUser(3L);
        givenValidCredentials(user, RAW_PASSWORD);
        givenStudentStatus(3L, StudentStatus.INACTIVE);

        assertThatThrownBy(() -> authService.login(loginRequest("student1", RAW_PASSWORD)))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("Student account is INACTIVE. Only ACTIVE students can sign in.");

        verify(jwtEncoder, never()).encode(any());
    }

    @Test
    void alumnusIsForbiddenAndGetsNoToken() {
        User user = studentUser(3L);
        givenValidCredentials(user, RAW_PASSWORD);
        givenStudentStatus(3L, StudentStatus.ALUMNI);

        assertThatThrownBy(() -> authService.login(loginRequest("student1", RAW_PASSWORD)))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("Student account is ALUMNI. Only ACTIVE students can sign in.");

        verify(jwtEncoder, never()).encode(any());
    }

    @Test
    void studentUserWithoutProfileIsForbidden() {
        User user = studentUser(3L);
        givenValidCredentials(user, RAW_PASSWORD);
        when(studentRepository.findByUserId(3L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(loginRequest("student1", RAW_PASSWORD)))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("Student profile for user 3 not found");

        verify(jwtEncoder, never()).encode(any());
    }

    @Test
    void wrongPasswordIsUnauthorized() {
        User user = studentUser(3L);
        when(userRepository.findByUsername("student1")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong-password", "hashed")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(loginRequest("student1", "wrong-password")))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Invalid username or password");

        verify(jwtEncoder, never()).encode(any());
        verify(studentRepository, never()).findByUserId(any());
    }

    @Test
    void adminLoginSkipsStudentStatusCheck() {
        User admin = user(1L, Role.ADMIN, "admin");
        givenValidCredentials(admin, RAW_PASSWORD);
        when(jwtEncoder.encode(any())).thenReturn(token("admin-token"));
        when(userService.getUserById(1L)).thenReturn(new UserResponseDto(1L, "admin", "ADMIN", "Admin"));

        LoginResponseDto response = authService.login(loginRequest("admin", RAW_PASSWORD));

        assertThat(response.getAccessToken()).isEqualTo("admin-token");
        verify(studentRepository, never()).findByUserId(any());
    }

    private void givenValidCredentials(User user, String rawPassword) {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, user.getPassword())).thenReturn(true);
    }

    private void givenStudentStatus(Long userId, StudentStatus status) {
        Student student = new Student();
        student.setId(10L);
        student.setStatus(status);
        when(studentRepository.findByUserId(userId)).thenReturn(Optional.of(student));
    }

    private User studentUser(Long id) {
        return user(id, Role.STUDENT, "student1");
    }

    private User user(Long id, Role role, String username) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword("hashed");
        user.setRole(role);
        user.setFullName(username);
        return user;
    }

    private LoginRequestDto loginRequest(String username, String password) {
        LoginRequestDto request = new LoginRequestDto();
        request.setUsername(username);
        request.setPassword(password);
        return request;
    }

    private Jwt token(String value) {
        return Jwt.withTokenValue(value).header("alg", "HS256").subject("1").build();
    }
}