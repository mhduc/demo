package com.example.auth.repository;

import com.example.auth.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    // Add custom queries if needed
}