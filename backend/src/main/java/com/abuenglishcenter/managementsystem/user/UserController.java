package com.abuenglishcenter.managementsystem.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import org.springframework.web.bind.annotation.*;

import com.abuenglishcenter.managementsystem.common.PageResponseDto;

@RestController 
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping 
    public PageResponseDto<UserResponseDto> getAllUsers(@RequestParam(defaultValue = "0") @Min(0) @Max(1000) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return userService.getAllUsers(page, size);
    }

    @PostMapping 
    public UserResponseDto createUser(@Valid @RequestBody UserCreateRequestDto request) {
        return userService.createUser(request);
    }
}
