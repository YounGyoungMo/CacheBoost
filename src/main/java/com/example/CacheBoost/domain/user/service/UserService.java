package com.example.CacheBoost.domain.user.service;


import static com.example.CacheBoost.domain.user.entity.Role.ADMIN;
import static com.example.CacheBoost.domain.user.entity.Role.USER;

import com.example.CacheBoost.common.exception.base.CustomException;
import com.example.CacheBoost.common.exception.enums.ErrorCode;
import com.example.CacheBoost.domain.auth.dto.TokenPayload;
import com.example.CacheBoost.domain.user.dto.RequestDto.UserRequestDto;
import com.example.CacheBoost.domain.user.dto.ResponseDto.UserResponseDto;
import com.example.CacheBoost.domain.user.entity.User;
import com.example.CacheBoost.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //  사용할 스레드 개수 8개
    private final ExecutorService executor = Executors.newFixedThreadPool(8);

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

    public UserResponseDto getUserByEmailV1(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return new UserResponseDto(user);
    }

    @Cacheable(value = "user", key = "#email")
    public UserResponseDto getUserByEmailV2(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return new UserResponseDto(user);
    }

    public User findByIdOrElseThrow(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public TokenPayload getTokenPayloadByUserId(Long userId) {
        User user = findByIdOrElseThrow(userId);
        return TokenPayload.builder()
            .userId(user.getId())
            .email(user.getEmail())
            .role(user.getRole())
            .build();
    }

    public void generateDummyAdminsParallel(int totalCount) {
        int batchSize = 5000;

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < totalCount; i += batchSize) {
            final int start = i;

            futures.add(
                CompletableFuture.runAsync(() -> insertBatch(start, batchSize, totalCount),
                    executor)
            );
        }
        // 모든 작업 완료까지 대기
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertBatch(int start, int batchSize, int totalCount) {
        List<User> users = new ArrayList<>();

        for (int i = start; i < start + batchSize && i < totalCount; i++) {
            users.add(User.builder()
                .name("관리자" + i)
                .email("admin" + i + "@example.com")
                .password("admin" + i)
                .role(ADMIN)
                .build());
        }

        userRepository.saveAll(users);
    }

    public void generateDummyUsersParallel(int totalCount) {
        int batchSize = 5000;

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < totalCount; i += batchSize) {
            final int start = i;

            futures.add(
                CompletableFuture.runAsync(() -> insertBatch2(start, batchSize, totalCount),
                    executor)
            );
        }
        // 모든 작업 완료까지 대기
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertBatch2(int start, int batchSize, int totalCount) {
        List<User> users = new ArrayList<>();

        for (int i = start; i < start + batchSize && i < totalCount; i++) {
            users.add(User.builder()
                .name("이용자" + i)
                .email("user" + i + "@example.com")
                .password("user" + i)
                .role(USER)
                .build());
        }

        userRepository.saveAll(users);
    }
}
