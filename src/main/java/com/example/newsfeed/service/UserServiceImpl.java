package com.example.newsfeed.service;

import com.example.newsfeed.dto.UserLoginResponseDto;
import com.example.newsfeed.dto.UserSignUpResponseDto;
import com.example.newsfeed.entity.User;
import com.example.newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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
}
