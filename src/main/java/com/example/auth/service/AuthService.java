package com.example.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.auth.dto.AuthResponse;
import com.example.auth.dto.ChangePasswordRequest;
import com.example.auth.dto.LoginRequest;
import com.example.auth.dto.RegisterRequest;
import com.example.auth.dto.UserInfoResponse;
import com.example.auth.entity.User;
import com.example.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.security.Principal;

// ... imports
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        // Kiểm tra email đã tồn tại chưa
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already in use");
        }
        var user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder().token(jwtToken).build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder().token(jwtToken).build();
    }
    
    public UserInfoResponse getUserInfo(Principal principal) {
        var user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        return UserInfoResponse.builder()
            .id(user.getId())
            .fullName(user.getFullName())
            .email(user.getEmail())
            .build();
    }

    public void changePassword(ChangePasswordRequest request, Principal principal) {
        var user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        // Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong old password");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}