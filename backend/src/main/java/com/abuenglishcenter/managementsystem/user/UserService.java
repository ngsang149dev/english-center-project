package com.abuenglishcenter.managementsystem.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service 
public class UserService {
    @Autowired 
    private UserRepository userRepository;

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::toDto).toList();
    }

    public UserResponseDto createUser(User user) {
        User saved = userRepository.save(user);
        return toDto(saved);
    }

    private UserResponseDto toDto(User user) {
        return new UserResponseDto(user.getId(), user.getUsername(), user.getRole().name(), user.getFullName());
    }
}
