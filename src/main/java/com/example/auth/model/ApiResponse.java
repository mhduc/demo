package com.example.auth.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // Không hiển thị các trường null trong JSON
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .code(0)
                .message("Success")
                .data(data)
                .build();
    }
    
    public static ApiResponse<Void> success() {
        return ApiResponse.<Void>builder()
                .code(0)
                .message("Success")
                .build();
    }

    public static ApiResponse<Object> error(int code, String message) {
        return ApiResponse.builder()
                .code(code)
                .message(message)
                .build();
    }
}