package com.example.CacheBoost.domain.user.service;


import com.example.CacheBoost.domain.user.dto.RequestDto.UserRequestDto;
import com.example.CacheBoost.domain.user.dto.ResponseDto.UserResponseDto;
import com.example.CacheBoost.domain.user.entity.User;
import com.example.CacheBoost.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
    public UserResponseDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return new UserResponseDto(user);
    }
}
