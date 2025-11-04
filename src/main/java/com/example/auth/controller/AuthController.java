package com.example.auth.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.auth.model.ApiResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.auth.dto.LoginRequest;
import com.example.auth.dto.RegisterRequest;
import com.example.auth.dto.JwtResponse;
import com.example.auth.service.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
// 📖 Thêm annotation Security cho Swagger
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }
    // Inject UserService, AuthenticationManager, JwtService...

    @PostMapping("/register")
    @Operation(summary = "Đăng ký tài khoản mới")
    public ResponseEntity<ApiResponse<?>> registerUser(@RequestBody RegisterRequest registerRequest) {
        // ... Logic kiểm tra tồn tại và lưu User vào DB (Mã hóa mật khẩu)
        // Trả về theo mẫu ApiResponse
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.<Void>builder()
                        .code(0)
                        .message("User registered successfully")
                        .build());
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            // 1. Dùng AuthenticationManager để xác thực username và password
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            // 2. Thiết lập đối tượng Authentication vào Security Context (tùy chọn, nhưng tốt cho context hiện tại)
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 3. Tạo JWT Access Token
            String jwt = jwtService.generateToken(authentication);

            // 4. Trả về Token cho Client theo mẫu ApiResponse
            return ResponseEntity.ok(ApiResponse.success(new JwtResponse(jwt, "Bearer")));
            
        } catch (Exception e) {
            // Xử lý ngoại lệ nếu xác thực thất bại (ví dụ: UsernameNotFoundException, BadCredentialsException)
            return ResponseEntity
                    .status(401) // Unauthorized
                    .body(ApiResponse.error(401, "Đăng nhập thất bại: Tên đăng nhập hoặc mật khẩu không đúng."));
        }
    }
}