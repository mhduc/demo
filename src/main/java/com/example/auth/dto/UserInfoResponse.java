package com.example.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoResponse {
    private Integer id;
    private String fullName;
    private String email;
}