package com.example.newsfeed.service;

import com.example.newsfeed.dto.UserLoginResponseDto;
import com.example.newsfeed.dto.UserResponseDto;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;

public interface UserService {
    UserResponseDto signUp(String name, String email, String password, LocalDate birth, int age);

    UserLoginResponseDto login(String email, String password);

    void updateUserPassword(Long userId, String oldPassword, String newPassword, Long sessionId);

    UserResponseDto updateUserInfo(Long userId, String name, LocalDate birth, int age, Long sessionId);

    void leave(Long userId, String password, HttpServletRequest request, Long sessionId);

    UserResponseDto findUserInfo(Long userId);

}
