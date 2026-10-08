package com.abuenglishcenter.managementsystem.user;

import org.springframework.web.bind.annotation.*;


import java.util.List;
@RestController 
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping 
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping 
    public UserResponseDto createUser(@RequestBody User user) {
        return userService.createUser(user);
    }
}

