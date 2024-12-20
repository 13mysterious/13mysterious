package com.example.newsfeed.controller;

import com.example.newsfeed.config.Const;
import com.example.newsfeed.config.PasswordEncoder;
import com.example.newsfeed.dto.*;
import com.example.newsfeed.service.UserService;
import com.example.newsfeed.util.ValidationUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    /**
     * 유저 가입 메서드
     *
     * @param requestDto 가입 시 필요한 정보
     * @return 가입에 성공한 유저 정보
     */
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signUp(
            @Valid @RequestBody UserSignUpRequestDto requestDto,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            ValidationUtils.bindErrorMessage(bindingResult);
        }

        UserResponseDto userSignUpResponseDto =
                userService.signUp(
                        requestDto.getName(),
                        requestDto.getEmail(),
                        passwordEncoder.encode(requestDto.getPassword()),
                        requestDto.getBirth(),
                        requestDto.getAge()
                );
        return new ResponseEntity<>(userSignUpResponseDto, HttpStatus.CREATED);
    }

    /**
     * 유저 비밀번호 변경 메서드
     *
     * @param userId
     * @param requestDto
     * @param sessionId
     * @return
     */
    @PatchMapping("/{userId}/password")
    public ResponseEntity<Void> updateUserPassword(
            @PathVariable Long userId,
            @Valid @RequestBody UserUpdatePasswordRequestDto requestDto,
            @SessionAttribute(name = Const.SESSION_KEY) Long sessionId,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ValidationUtils.bindErrorMessage(bindingResult);
        }

        userService.updateUserPassword(userId, requestDto.getOldPassword(), requestDto.getNewPassword(), sessionId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 유저 정보 변경 메서드
     *
     * @param userId
     * @param requestDto
     * @param sessionId
     * @return
     */
    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUserInfo(
            @PathVariable Long userId,
            @Valid @RequestBody UserUpdateInfoRequestDto requestDto,
            @SessionAttribute(name = Const.SESSION_KEY) Long sessionId,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ValidationUtils.bindErrorMessage(bindingResult);
        }

        UserResponseDto userResponseDto = userService.updateUserInfo(userId, requestDto.getName(), requestDto.getBirth(), requestDto.getAge(), sessionId);

        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    /**
     * 유저 탈퇴 메서드
     *
     * @param userId     유저 식별자
     * @param requestDto 탈퇴 시 필요한 정보
     * @return 탈퇴 시점 저장
     */
    @PatchMapping("/{userId}/leave")
    public ResponseEntity<UserResponseDto> leaveUserInfo(
            @PathVariable Long userId,
            @Valid @RequestBody UserLeaveRequestDto requestDto,
            HttpServletRequest request,
            @SessionAttribute(name = Const.SESSION_KEY) Long sessionId,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ValidationUtils.bindErrorMessage(bindingResult);
        }

        userService.leave(userId, requestDto.getPassword(), request, sessionId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 유저 정보 조회
     *
     * @param userId 유저 식별자
     * @return 유저 조회 정보
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserInfo(
            @PathVariable Long userId
    ) {
        UserResponseDto userResponseDto = userService.findUserInfo(userId);

        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }
}
