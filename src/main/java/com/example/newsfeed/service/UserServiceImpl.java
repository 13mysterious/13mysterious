package com.example.newsfeed.service;

import com.example.newsfeed.dto.UserSignUpResponseDto;
import com.example.newsfeed.entity.User;
import com.example.newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
