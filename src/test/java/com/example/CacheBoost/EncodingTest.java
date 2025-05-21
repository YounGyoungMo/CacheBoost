package com.example.CacheBoost;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;

public class EncodingTest {

    PasswordEncoder passwordEncoder  = new BCryptPasswordEncoder();

    @Test
    void encoding_Test() {
        String rawAdminPassword = "admin123";
        String rawUserPassword = "test123";

        String gpt_encoded = "$2a$10$6PJi4zO6NlKknHbYg7Tf3u5Y2mGLRIy2fxWj89vTj5c4sNhqgLsIe";
        String encodedAdmin = passwordEncoder.encode(rawAdminPassword);
        String encodedUser = passwordEncoder.encode(rawUserPassword);
//        System.out.println("encodedUser = " + encodedUser);

//        System.out.println(encodedAdmin);
        assertThat(encodedAdmin).isNotEqualTo(rawAdminPassword);
        assertThat(passwordEncoder.matches(rawAdminPassword, encodedAdmin)).isTrue();

//        assertThat(passwordEncoder.matches(rawPassword, encoded)).isTrue();
//        assertThat(passwordEncoder.matches(rawPassword, gpt_encoded)).isTrue();
//        assertThat(encodedAdmin).isEqualTo(gpt_encoded);
    }
}
