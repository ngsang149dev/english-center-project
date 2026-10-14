package com.abuenglishcenter.managementsystem.auth;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.abuenglishcenter.managementsystem.user.User;
import com.abuenglishcenter.managementsystem.user.UserRepository;
import com.abuenglishcenter.managementsystem.user.UserService;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final long expiresInMinutes;
    private final UserService userService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder,
            @Value("${app.jwt.expires-in-minutes}") long expiresInMinutes, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
        this.expiresInMinutes = expiresInMinutes;
        this.userService = userService;
    }

    public LoginResponseDto login(LoginRequestDto request) {
        User checkUser = userRepository.findByUsername(request.getUsername())
                .filter(u -> passwordEncoder.matches(request.getPassword(), u.getPassword()))
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(String.valueOf(checkUser.getId())).issuedAt(now)
                .expiresAt(now.plus(expiresInMinutes, ChronoUnit.MINUTES)).claim("role", checkUser.getRole().name())
                .build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims)).getTokenValue();

        return new LoginResponseDto(token,"Bearer" , expiresInMinutes * 60,userService.getUserById(checkUser.getId()));
    }

}
