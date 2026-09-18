
package com.abuenglishcenter.managementsystem.user;

public class UserResponseDto {
    private Long id;
    private String username;
    private String role;
    private String fullName;

    public UserResponseDto(Long id, String username, String role, String fullName) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public String getFullName() {
        return fullName;
    }

}