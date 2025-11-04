package com.example.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.auth.dto.ChangePasswordRequest;
import com.example.auth.model.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "Bearer Authentication") // Yêu cầu token cho toàn bộ Controller
@Tag(name = "User Management", description = "Các API yêu cầu xác thực")
public class UserController {

    // Inject UserService...

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // 🔒 Chỉ Admin mới được truy cập
    @Operation(summary = "Lấy danh sách người dùng (Chỉ Admin)")
    public ResponseEntity<ApiResponse<?>> getAllUsers() {
        // ... Logic lấy danh sách User từ DB
        // Trả về dữ liệu theo mẫu ApiResponse. Ở đây placeholder trả success không có data.
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PutMapping("/change-password")
    @Operation(summary = "Đổi mật khẩu")
    public ResponseEntity<ApiResponse<?>> changePassword(@RequestBody ChangePasswordRequest request) {
        // Lấy User hiện tại từ Spring Security Context
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // ... Logic đổi mật khẩu
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .code(0)
                .message("Password changed successfully.")
                .build());
    }
}