package com.abuenglishcenter.managementsystem.auth;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abuenglishcenter.managementsystem.user.UserResponseDto;
import com.abuenglishcenter.managementsystem.user.UserService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public UserResponseDto me(@AuthenticationPrincipal Jwt jwt) {
        return userService.getUserById(Long.valueOf(jwt.getSubject()));
    }
}
