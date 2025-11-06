package com.example.auth.util;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility để tạo các chuỗi ngẫu nhiên, an toàn.
 */
public final class RandomString {

    private RandomString() {} // Ngăn tạo instance

    /**
     * Tạo một chuỗi ngẫu nhiên (token/key) có độ dài 32 ký tự (Base64 URL-safe encoded).
     * @param byteLength Độ dài của mảng byte ngẫu nhiên (ví dụ: 24 bytes sẽ ra ~32 ký tự Base64).
     * @return Chuỗi ngẫu nhiên an toàn.
     */
    public static String generateRandomKey(int byteLength) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[byteLength];
        random.nextBytes(bytes);
        
        // Sử dụng Base64 URL-safe để đảm bảo chuỗi không có ký tự đặc biệt
        // 24 bytes sau khi encode Base64 sẽ có độ dài 32 ký tự
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
    
    /**
     * Tạo một auth_key 32 ký tự.
     */
    public static String generateAuthKey() {
        // Cần 24 bytes để Base64 encode ra chuỗi 32 ký tự (24 * 4/3 = 32)
        return generateRandomKey(24); 
    }
}