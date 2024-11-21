package com.example.newsfeed.controller;

import com.example.newsfeed.dto.UserLoginRequestDto;
import com.example.newsfeed.dto.UserLoginResponseDto;
import com.example.newsfeed.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    /**
     * 로그인 메서드
     *
     * @param requestDto 로그인에 필요한 유저 정보
     * @param request    유저 정보를 저장할 객체
     * @return 로그인 성공한 유저 정보
     */
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody UserLoginRequestDto requestDto, HttpServletRequest request) {

        UserLoginResponseDto responseDto = userService.login(requestDto.getEmail(), requestDto.getPassword());

        Long userId = responseDto.getId();

        HttpSession session = request.getSession();

        session.setAttribute("userId", userId);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * 로그아웃 메서드
     *
     * @param request 유저 정보가 저장된 객체
     * @return 200
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
