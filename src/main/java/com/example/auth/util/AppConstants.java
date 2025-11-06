package com.example.auth.util;

public interface AppConstants {

    // --- Cấu hình Mặc định (Default Configurations) ---
    /**
     * Mặc định phân trang: Kích thước trang.
     */
    int DEFAULT_PAGE_SIZE = 10;

    /**
     * Mặc định phân trang: Số trang bắt đầu.
     */
    int DEFAULT_PAGE_NUMBER = 0;

    /**
     * Mặc định phân trang: Trường sắp xếp.
     */
    String DEFAULT_SORT_BY = "id";

    /**
     * Token type: Access Token
     */
    int TOKEN_TYPE_ACCESS = 1;

    /**
     * Token type: Refresh Token
     */
    int TOKEN_TYPE_REFRESH = 2;

    // --- Giá trị Security/Token ---
    /**
     * Loại mã thông báo (token type) - Ví dụ: "Bearer".
     */
    String TOKEN_TYPE = "Bearer ";

    /**
     * Mặc định vai trò người dùng mới đăng ký.
     */
    String DEFAULT_USER_ROLE = "USER";

    // --- Địa chỉ API ---
    /**
     * Đường dẫn cơ sở cho các API liên quan đến Authentication.
     */
    String AUTH_API_BASE_URL = "/api/v1/auth";
}