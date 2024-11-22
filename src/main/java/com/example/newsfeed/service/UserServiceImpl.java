package com.example.newsfeed.service;

import com.example.newsfeed.dto.UserLoginResponseDto;
import com.example.newsfeed.dto.UserResponseDto;
import com.example.newsfeed.entity.User;
import com.example.newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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
        if (findUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
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
        if (findUser.isEmpty() || !password.equals(findUser.get().getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
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
                new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾지 못했습니다."));

        // 로그인한 유저가 다른 유저 정보를 조회할 경우
        if (userId != sessionId) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 접근입니다.");
        }

        // 비밀번호가 일치하지 않는 경우
        if (!findUser.getPassword().equals(oldPassword)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀 번호가 일치하지 않습니다.");
        }

        // 현재 비밀번호와 새 비밀번호가 같을 경우
        if (oldPassword.equals(newPassword)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        findUser.updateUserPassword(newPassword);

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
                new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾지 못했습니다."));

        // 로그인한 유저가 다른 유저 정보를 조회할 경우
        if (userId != sessionId) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 접근입니다.");
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
    public void leave(Long userId, String password) {

        // 유저 식별자로 유저 조회
        User findUser = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾지 못했습니다."));

        // 비밀번호가 다를 경우
        if (!findUser.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        // 탈퇴 날짜 저장
        findUser.leaveUser(LocalDate.now());
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
                new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾지 못했습니다."));

        return new UserResponseDto(
                findUser.getId(),
                findUser.getName(),
                findUser.getEmail(),
                findUser.getBirth(),
                findUser.getAge()
        );
    }
}
