package com.example.CacheBoost.domain.user.entity;

import com.example.CacheBoost.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Generated;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    // TODO 비밀번호는 암호화시켜서 저장
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime deletedAt;
}
