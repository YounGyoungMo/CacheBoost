package com.example.CacheBoost.domain.user.repository;

import com.example.CacheBoost.common.exception.base.CustomException;
import com.example.CacheBoost.common.exception.enums.ErrorCode;
import com.example.CacheBoost.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long userId);
    default User findByIdOrElseThrow(Long userId) {
        return findById(userId).orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    Optional<User> findByEmail(String email);
}
