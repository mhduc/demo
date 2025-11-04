package com.example.auth.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
// Định nghĩa thông tin chung của API
@OpenAPIDefinition(
    info = @Info(
        title = "Auth API (JWT) Documentation", 
        version = "1.0",
        description = "Tài liệu hóa các API xác thực và quản lý người dùng."
    )
)
// 💡 Định nghĩa Security Scheme (Bearer Token)
@SecurityScheme(
  name = "Bearer Authentication", // Tên tham chiếu
  type = SecuritySchemeType.HTTP,
  bearerFormat = "JWT",
  scheme = "bearer",
  description = "Nhập JWT Access Token vào đây (ví dụ: 'eyJhbGciOiJIUzI1NiIsInR5c...')"
)
public class OpenAPIConfig {
    // Class này chỉ cần các annotations để cấu hình
}