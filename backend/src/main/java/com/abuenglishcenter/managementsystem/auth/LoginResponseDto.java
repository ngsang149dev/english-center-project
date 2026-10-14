package com.abuenglishcenter.managementsystem.auth;

import com.abuenglishcenter.managementsystem.user.UserResponseDto;

public class LoginResponseDto {
    private String accessToken;
    private String tokenType;
    private long expiresIn;
    private UserResponseDto user;

    public LoginResponseDto(String accessToken, String tokenType, long expiresIn, UserResponseDto user) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }
    public String getTokenType() {
        return tokenType;
    }
    public long getExpiresIn() {
        return expiresIn;
    }
    public UserResponseDto getUser() {
        return user;
    } 

    
}
