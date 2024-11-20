package com.example.newsfeed.controller;

import com.example.newsfeed.dto.UserSignUpRequestDto;
import com.example.newsfeed.dto.UserSignUpResponseDto;
import com.example.newsfeed.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserSignUpResponseDto> signUp(@RequestBody UserSignUpRequestDto requestDto) {

        UserSignUpResponseDto userSignUpResponseDto =
                userService.signUp(
                        requestDto.getEmail(),
                        requestDto.getName(),
                        requestDto.getPassword(),
                        requestDto.getBirth(),
                        requestDto.getAge()
                );
        return new ResponseEntity<>(userSignUpResponseDto, HttpStatus.CREATED);
    }
}
