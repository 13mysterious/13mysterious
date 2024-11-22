package com.example.newsfeed.service;

import com.example.newsfeed.config.PasswordEncoder;
import com.example.newsfeed.dto.UserLoginResponseDto;
import com.example.newsfeed.dto.UserResponseDto;
import com.example.newsfeed.entity.User;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorCode;
import com.example.newsfeed.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 유저 가입 메서드
     *
     * @param name          유저 이름
     * @param email         유저 이메일
     * @param password      유저 비밀번호
     * @param birth         유저 생일
     * @param age           유저 나이
     * @return
     */
    @Override
    public UserResponseDto signUp(String name, String email, String password, LocalDate birth, int age) {

        Optional<User> findUser = userRepository.findUserByEmail(email);

        // 탈퇴한 유저일 경우
        if (findUser.isPresent() && findUser.get().getLeaveDate() != null) {
            throw new CustomException(ErrorCode.INVALID_LEAVE_USER);
        } else if (findUser.isPresent()) { // 동일한 이메일이 있는 경우
            throw new CustomException(ErrorCode.INVALID_INPUT_EMAIL);
        }

        // 받아온 유저 정보를 변수에 저장
        User user = new User(name, email, password, birth, age);

        // repository 에 저장
        User savedUser = userRepository.save(user);

        return new UserResponseDto(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getBirth(),
                savedUser.getAge()
        );
    }

    /**
     * 로그인 메서드
     *
     * @param email         유저 가입한 이메일
     * @param password      유저 가입한 비밀번호
     * @return
     */
    @Override
    public UserLoginResponseDto login(String email, String password) {

        // 이메일로 유저 정보 조회
        Optional<User> findUser = userRepository.findUserByEmail(email);

        // 이메일이 다르거나 비밀번호가 다른 경우
        if (findUser.isEmpty() ) {
            throw new CustomException(ErrorCode.INVALID_EMAIL);
        } else if(!passwordEncoder.matches(password,findUser.get().getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        } else if(findUser.get().getLeaveDate()!=null){
            throw new CustomException(ErrorCode.INVALID_LEAVE_USER);
        }

        return new UserLoginResponseDto(findUser.get().getId(), findUser.get().getEmail());

    }

    /**
     * 비밀번호 수정 메서드
     *
     * @param userId            수정 하고 싶은 유저 식별자
     * @param oldPassword       현재 비밀번호
     * @param newPassword       새 비밀번호
     * @param sessionId         현재 로그인 중인 유저 식별자
     */
    @Transactional
    @Override
    public void updateUserPassword(Long userId, String oldPassword, String newPassword, Long sessionId) {

        // 유저 식별자로 유저 조회
        User findUser = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.USER_NOT_FOUND));

        // 로그인한 유저가 다른 유저 정보를 조회할 경우
        if (userId != sessionId) {
            throw new CustomException(ErrorCode.INVALID_SESSION_ID);
        }

        // 비밀번호가 일치하지 않는 경우
        if (!passwordEncoder.matches(oldPassword,findUser.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        // 현재 비밀번호와 새 비밀번호가 같을 경우
        if (oldPassword.equals(newPassword)) {
            throw new CustomException(ErrorCode.INVALID_INPUT_PASSWORD);
        }

        findUser.updateUserPassword(passwordEncoder.encode(newPassword));

    }

    /**
     * 유저 정보 수정 메서드
     *
     * @param userId        수정할 유저 식별자
     * @param name          수정할 이름
     * @param birth         수정할 생일
     * @param age           수정할 나이
     * @param sessionId     현재 로그인 중인 유저 식별자
     * @return
     */
    @Transactional
    @Override
    public UserResponseDto updateUserInfo(Long userId, String name, LocalDate birth, int age, Long sessionId) {

        // 유저 식별자로 유저 조회
        User findUser = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.USER_NOT_FOUND));

        // 로그인한 유저가 다른 유저 정보를 조회할 경우
        if (userId != sessionId) {
            throw new CustomException(ErrorCode.INVALID_SESSION_ID);
        }

        // 유저 정보 수정
        findUser.updateUserInfo(name, birth, age);

        return new UserResponseDto(
                findUser.getId(),
                findUser.getEmail(),
                findUser.getName(),
                findUser.getBirth(),
                findUser.getAge()
        );
    }

    /**
     *
     * @param userId        탈퇴할 유저 식별자
     * @param password      탈퇴할 유저 비밀번호
     */
    @Transactional
    @Override
    public void leave(Long userId, String password, HttpServletRequest request) {

        // 유저 식별자로 유저 조회
        User findUser = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.USER_NOT_FOUND));

        // 비밀번호가 다를 경우
        if (!passwordEncoder.matches(password,findUser.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        // 탈퇴 날짜 저장
        findUser.leaveUser(LocalDate.now());

        // 로그아웃 처리
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }
    }

    /**
     *
     * @param userId    조회할 유저 식별자
     * @return
     */
    @Override
    public UserResponseDto findUserInfo(Long userId) {

        // 유저 식별자로 유저 조회
        User findUser = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.USER_NOT_FOUND));

        return new UserResponseDto(
                findUser.getId(),
                findUser.getName(),
                findUser.getEmail(),
                findUser.getBirth(),
                findUser.getAge()
        );
    }
}
