package com.example.newsfeed.controller;

import com.example.newsfeed.dto.UserPatchPasswordRequestDto;
import com.example.newsfeed.dto.UserSignUpRequestDto;
import com.example.newsfeed.dto.UserSignUpResponseDto;
import com.example.newsfeed.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserSignUpResponseDto> signUp(@RequestBody UserSignUpRequestDto requestDto) {

        UserSignUpResponseDto userSignUpResponseDto =
                userService.signUp(
                        requestDto.getName(),
                        requestDto.getEmail(),
                        requestDto.getPassword(),
                        requestDto.getBirth(),
                        requestDto.getAge()
                );
        return new ResponseEntity<>(userSignUpResponseDto, HttpStatus.CREATED);
    }

    @PatchMapping("/{userId}/password")
    public ResponseEntity<Void> updateUserPassword(
            @PathVariable Long userId,
            @RequestBody UserPatchPasswordRequestDto requestDto,
            @SessionAttribute(name = "userId") Long sessionId
    ){
        userService.updateUserPassword(userId, requestDto.getOldPassword(), requestDto.getNewPassword(),sessionId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
