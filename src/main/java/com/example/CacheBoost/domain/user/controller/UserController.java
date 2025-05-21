package com.example.CacheBoost.domain.user.controller;

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

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }
}
