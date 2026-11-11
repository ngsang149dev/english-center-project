package com.abuenglishcenter.managementsystem.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abuenglishcenter.managementsystem.common.PageResponseDto;
import com.abuenglishcenter.managementsystem.exception.BusinessRuleException;
import com.abuenglishcenter.managementsystem.exception.NotFoundException;

@Service 
public class UserService {
    private final UserRepository userRepository; 
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public PageResponseDto<UserResponseDto> getAllUsers(int page, int size) {
        return PageResponseDto.of(userRepository.findAll(PageResponseDto.pageable(page, size)), this::toDto);
    }


    private UserResponseDto toDto(User user) {
        return new UserResponseDto(user.getId(), user.getUsername(), user.getRole().name(), user.getFullName());
    }

    public UserResponseDto getUserById(Long id) {
        User checkUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
        return toDto(checkUser);
    }

    @Transactional 
    public UserResponseDto createUser(UserCreateRequestDto request) {
        if (request.getRole() != Role.ADMIN) {
            throw new BusinessRuleException("Only ADMIN accounts can be created here. Use POST /teachers or POST /students");
        }
        
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessRuleException("Username '" + request.getUsername() +"' already exists");
        }
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setFullName(request.getFullName());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(Role.ADMIN);
        User saved = userRepository.save(newUser);
        return toDto(saved);
    }
}
