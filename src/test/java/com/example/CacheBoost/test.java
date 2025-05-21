package com.example.CacheBoost;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;

public class test {

    PasswordEncoder passwordEncoder  = new BCryptPasswordEncoder();

    @Test
    void encoding_Test() {
        String rawPassword = "password123";

        String gpt_encoded = "$2a$10$6PJi4zO6NlKknHbYg7Tf3u5Y2mGLRIy2fxWj89vTj5c4sNhqgLsIe";
        String encoded = passwordEncoder.encode(rawPassword);

        System.out.println(encoded);
        assertThat(encoded).isNotEqualTo(rawPassword);

//        assertThat(passwordEncoder.matches(rawPassword, encoded)).isTrue();
//        assertThat(passwordEncoder.matches(rawPassword, gpt_encoded)).isTrue();
        assertThat(encoded).isEqualTo(gpt_encoded);
    }
}
