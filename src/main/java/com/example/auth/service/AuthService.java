package com.example.auth.service;

import java.util.Objects;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.auth.dto.AuthResponse;
import com.example.auth.dto.ChangePasswordRequest;
import com.example.auth.dto.LoginRequest;
import com.example.auth.dto.RegisterRequest;
import com.example.auth.dto.UserInfoResponse;
import com.example.auth.entity.User;
import com.example.auth.entity.UserToken;
import com.example.auth.repository.UserRepository;
import com.example.auth.repository.UserTokenRepository;
import com.example.auth.util.AppConstants;

import lombok.RequiredArgsConstructor;

import java.security.Principal;

// ... imports
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        // Kiểm tra email đã tồn tại chưa
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already in use");
        }
        var user = User.builder()
                .fullname(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        user = userRepository.save(Objects.requireNonNull(user, "User must not be null"));
        
        var authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        var accessToken = jwtService.generateToken(authentication);
        var refreshToken = jwtService.generateRefreshToken(authentication);
        
        // Tạo bản ghi cho access token
        var accessTokenRecord = UserToken.builder()
                .user(user)
                .accessToken(accessToken)
                .type(AppConstants.TOKEN_TYPE_ACCESS)
                .build();
        userTokenRepository.save(Objects.requireNonNull(accessTokenRecord, "Access token record must not be null"));

        // Tạo bản ghi cho refresh token
        var refreshTokenRecord = UserToken.builder()
                .user(user)
                .accessToken(refreshToken)
                .type(AppConstants.TOKEN_TYPE_REFRESH)
                .build();
        userTokenRepository.save(Objects.requireNonNull(refreshTokenRecord, "Refresh token record must not be null"));
        
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        var accessToken = jwtService.generateToken(authentication);
        var refreshToken = jwtService.generateRefreshToken(authentication);
        
        // Lưu token vào database
        var user = userRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found"));
        
        // Tạo bản ghi cho access token
        var accessTokenRecord = UserToken.builder()
                .user(user)
                .accessToken(accessToken)
                .type(AppConstants.TOKEN_TYPE_ACCESS)
                .build();
        userTokenRepository.save(accessTokenRecord);

        // Tạo bản ghi cho refresh token
        var refreshTokenRecord = UserToken.builder()
                .user(user)
                .accessToken(refreshToken)
                .type(AppConstants.TOKEN_TYPE_REFRESH)
                .build();
        userTokenRepository.save(refreshTokenRecord);
        
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
    
    public UserInfoResponse getUserInfo(Principal principal) {
        var user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        return UserInfoResponse.builder()
            .id(user.getId())
            .fullName(user.getFullname())
            .email(user.getEmail())
            .build();
    }

    public void changePassword(ChangePasswordRequest request, Principal principal) {
        var user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        // Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong old password");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}