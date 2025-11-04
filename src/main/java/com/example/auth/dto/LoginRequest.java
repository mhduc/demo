package com.example.auth.dto;
// ... imports

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    
    @Email 
    @NotBlank
    @Schema(description = "Địa chỉ email của người dùng", example = "john.doe@example.com")
    private String username;
    
    @NotBlank
    @Schema(description = "Mật khẩu", example = "Pa$$w0rd")
    private String password;
}