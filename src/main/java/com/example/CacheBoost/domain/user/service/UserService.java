package com.example.CacheBoost.domain.user.service;


import com.example.CacheBoost.common.exception.base.CustomException;
import com.example.CacheBoost.common.exception.enums.ErrorCode;
import com.example.CacheBoost.domain.user.dto.RequestDto.UserRequestDto;
import com.example.CacheBoost.domain.user.dto.ResponseDto.UserResponseDto;
import com.example.CacheBoost.domain.user.entity.User;
import com.example.CacheBoost.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto signup(UserRequestDto userRequestDto) {
        User user = User.builder()
                .name(userRequestDto.getName())
                .email(userRequestDto.getEmail())
                .password(passwordEncoder.encode(userRequestDto.getPassword()))
                .role(userRequestDto.getRole())
                .build();


        userRepository.save(user);
        return new UserResponseDto(user);

    }

    @Cacheable(value = "user", key = "#email")
    public UserResponseDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return new UserResponseDto(user);
    }

    public User findByIdOrElseThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
