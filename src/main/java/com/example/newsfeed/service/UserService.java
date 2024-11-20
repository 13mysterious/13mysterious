package com.example.newsfeed.service;

import com.example.newsfeed.dto.UserLoginResponseDto;
import com.example.newsfeed.dto.UserSignUpResponseDto;

import java.time.LocalDate;

public interface UserService {
    UserSignUpResponseDto signUp(String name, String email, String password, LocalDate birth, int age);

    UserLoginResponseDto login(String email, String password);
    void updateUserPassword(Long userId, String oldPassword, String newPassword, Long sessionId);

    UserSignUpResponseDto updateUserInfo(Long userId, String name, LocalDate birth, int age, Long sessionId);
}
