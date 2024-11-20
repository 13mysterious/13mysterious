package com.example.newsfeed.service;

import com.example.newsfeed.dto.UserSignUpResponseDto;
import com.example.newsfeed.entity.User;
import com.example.newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserSignUpResponseDto signUp(String email, String name, String password, LocalDate birth, int age) {

        User user = new User(email, name, password, birth, age);

        User savedUser = userRepository.save(user);

        return new UserSignUpResponseDto(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getBirth(),
                savedUser.getAge()
        );
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
}
