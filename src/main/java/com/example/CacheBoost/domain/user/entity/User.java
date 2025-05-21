package com.example.CacheBoost.domain.user.entity;

import com.example.CacheBoost.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
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
    
    // 임의로 만든 User 생성자 (삭제 예정)
    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
