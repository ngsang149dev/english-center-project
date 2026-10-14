package com.abuenglishcenter.managementsystem.user;

import org.springframework.stereotype.Service;

import com.abuenglishcenter.managementsystem.exception.NotFoundException;

import java.util.List;

@Service 
public class UserService {
    private final UserRepository userRepository; 

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;   
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::toDto).toList();
    }


    private UserResponseDto toDto(User user) {
        return new UserResponseDto(user.getId(), user.getUsername(), user.getRole().name(), user.getFullName());
    }

    public UserResponseDto getUserById(Long id) {
        User checkUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with id" + id + "not found"));
        return toDto(checkUser);
    }
}
