package com.example.CacheBoost.domain.user.controller;

import com.example.CacheBoost.common.exception.enums.SuccessCode;
import com.example.CacheBoost.common.response.ApiResponseDto;
import com.example.CacheBoost.domain.user.dto.RequestDto.UserRequestDto;
import com.example.CacheBoost.domain.user.dto.ResponseDto.UserResponseDto;
import com.example.CacheBoost.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(userService.signup(userRequestDto));
    }

    @GetMapping("/users/v1/{email}")
    public ResponseEntity<UserResponseDto> getUserByEmailV1(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmailV1(email));
    }

    @GetMapping("/users/v2/{email}")
    public ResponseEntity<UserResponseDto> getUserByEmailV2(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmailV2(email));
    }

    @PostMapping("/signup/admin-dummy-generate")
    public ResponseEntity<ApiResponseDto<String>> generateAdminsDummy(@RequestParam(defaultValue = "1000") int count) {
        userService.generateDummyAdminsParallel(count);
        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.DUMMY_BOOKS_CREATED));
    }

    @PostMapping("/signup/user-dummy-generate")
    public ResponseEntity<ApiResponseDto<String>> generateDummyUsers(@RequestParam(defaultValue = "1000") int count) {
        userService.generateDummyUsersParallel(count);
        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.DUMMY_BOOKS_CREATED));
    }
}
