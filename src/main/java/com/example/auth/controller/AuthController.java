package com.example.auth.controller;

import com.example.auth.dto.*;
import com.example.auth.model.ApiResponse;
import com.example.auth.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

// Spring Framework imports
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

// Lombok import
import lombok.RequiredArgsConstructor;

// Java imports
import jakarta.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Xác thực & Người dùng", description = "Quản lý Đăng ký, Đăng nhập, và Thông tin Người dùng")
public class AuthController {

    private final AuthService authService;

    // --- 1. REGISTER ---
    @Operation(summary = "Đăng ký tài khoản mới", 
               description = "Tạo người dùng mới và trả về JWT Token.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Đăng ký thành công",
        content = @Content(schema = @Schema(implementation = AuthResponse.class)))
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Email đã tồn tại / Dữ liệu không hợp lệ")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(ApiResponse.success(authService.register(request)));
    }

    // --- 2. LOGIN ---
    @Operation(summary = "Đăng nhập", 
               description = "Xác thực người dùng và cấp JWT Token.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Đăng nhập thành công",
        content = @Content(schema = @Schema(implementation = AuthResponse.class)))
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Email hoặc Mật khẩu không đúng")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success(authService.login(request)));
    }

    // --- 3. GET USER INFO ---
    @Operation(summary = "Lấy thông tin người dùng hiện tại", 
               description = "Yêu cầu JWT Token hợp lệ trong Header Authorization.",
               security = @SecurityRequirement(name = "bearerAuth")) // Đánh dấu là API cần bảo mật
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lấy thông tin thành công",
        content = @Content(schema = @Schema(implementation = UserInfoResponse.class)))
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Token không hợp lệ hoặc đã hết hạn")
    @GetMapping("/info")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getUserInfo(Principal principal) {
        return ResponseEntity.ok(ApiResponse.success(authService.getUserInfo(principal)));
    }
    
    // --- 4. CHANGE PASSWORD ---
    @Operation(summary = "Đổi mật khẩu", 
               description = "Yêu cầu JWT Token. Cần mật khẩu cũ và mật khẩu mới.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Đổi mật khẩu thành công")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Token không hợp lệ / Mật khẩu cũ không đúng")
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request, Principal principal) {
        authService.changePassword(request, principal);
        return ResponseEntity.ok(ApiResponse.success());
    }

    // --- 5. LOGOUT ---
    @Operation(summary = "Đăng xuất (Client side)",
               description = "Với JWT, đây là thao tác tượng trưng. Client nên xóa token đã lưu trữ.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Thành công")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        return ResponseEntity.ok(ApiResponse.success());
    }
}