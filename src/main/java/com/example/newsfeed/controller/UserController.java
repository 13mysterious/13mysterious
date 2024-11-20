package com.example.newsfeed.controller;

import com.example.newsfeed.dto.*;
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

    @PatchMapping("/{userId}")
    public ResponseEntity<UserSignUpResponseDto> updateUserInfo(
            @PathVariable Long userId,
            @RequestBody UserPatchInfoRequestDto requestDto,
            @SessionAttribute(name = "userId") Long sessionId
    ){
        UserSignUpResponseDto userSignUpResponseDto = userService.updateUserInfo(userId, requestDto.getName(), requestDto.getBirth(), requestDto.getAge(), sessionId);

        return new ResponseEntity<>(userSignUpResponseDto,HttpStatus.OK);
    }

    @PatchMapping("/{userId}/leave")
    public ResponseEntity<UserSignUpResponseDto> leaveUserInfo(
            @PathVariable Long userId,
            @RequestBody UserLeaveRequestDto requestDto
    ) {
        userService.leave(userId, requestDto.getPassword());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
