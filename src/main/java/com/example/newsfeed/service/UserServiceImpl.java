package com.example.newsfeed.service;

import com.example.newsfeed.dto.UserLoginResponseDto;
import com.example.newsfeed.dto.UserSignUpResponseDto;
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

    @Override
    public UserSignUpResponseDto signUp(String name, String email, String password, LocalDate birth, int age) {

        User user = new User(name, email, password, birth, age);

        User savedUser = userRepository.save(user);

        return new UserSignUpResponseDto(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getBirth(),
                savedUser.getAge()
        );
    }

    @Override
    public UserLoginResponseDto login(String email, String password) {

        Optional<User> findUser = userRepository.findUserByEmail(email);

        if (findUser.isEmpty() || !password.equals(findUser.get().getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return new UserLoginResponseDto(findUser.get().getId(), findUser.get().getEmail());

    }
    
    @Transactional
    @Override
    public void updateUserPassword(Long userId, String oldPassword, String newPassword, Long sessionId) {
        User findUser = userRepository.findById(userId).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND,"유저를 찾지 못했습니다."));

        if(userId != sessionId){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"잘못된 접근입니다.");
        }

        if(!findUser.getPassword().equals(oldPassword)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"비밀 번호가 일치하지 않습니다.");
        }

        findUser.updateUserPassword(newPassword);

    }

    @Transactional
    @Override
    public UserSignUpResponseDto updateUserInfo(Long userId, String name, LocalDate birth, int age, Long sessionId) {
        User findUser = userRepository.findById(userId).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND,"유저를 찾지 못했습니다."));

        if(userId != sessionId){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"잘못된 접근입니다.");
        }

        findUser.updateUserInfo(name,birth,age);

        return new UserSignUpResponseDto(findUser.getId(), findUser.getEmail(), findUser.getName(), findUser.getBirth(),findUser.getAge());
    }

    @Transactional
    @Override
    public void leave(Long userId, String password) {
        User findUser = userRepository.findById(userId).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND,"유저를 찾지 못했습니다."));

        if (!findUser.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        findUser.leaveUser(LocalDate.now());
    }
}
