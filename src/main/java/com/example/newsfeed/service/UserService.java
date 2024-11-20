package com.example.newsfeed.service;

import com.example.newsfeed.dto.UserSignUpResponseDto;

import java.time.LocalDate;

public interface UserService {
    UserSignUpResponseDto signUp(String email, String name, String password, LocalDate birth, int age);

}
