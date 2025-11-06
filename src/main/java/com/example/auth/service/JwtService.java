package com.example.auth.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    // 🔒 Khóa bí mật JWT (Nên lấy từ Environment Variable hoặc application.properties)
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    // ⏰ Thời gian sống của Token (ví dụ: 24 giờ)
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    /**
     * 1. Tạo JWT Token từ đối tượng Authentication
     */
    public String generateToken(Authentication authentication) {
        return generateToken(authentication, jwtExpiration);
    }

    public String generateRefreshToken(Authentication authentication) {
        return generateToken(authentication, refreshExpiration);
    }

    private String generateToken(Authentication authentication, long expiration) {
        // Lấy UserDetails từ đối tượng Authentication
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        
        return Jwts.builder()
            // 👤 Chủ thể của Token là Username
            .setSubject(userPrincipal.getUsername())
            // 📅 Thời gian tạo
            .setIssuedAt(new Date(System.currentTimeMillis()))
            // ⏱️ Thời gian hết hạn
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            // 🔐 Ký Token bằng khóa bí mật
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * 2. Trích xuất Username từ JWT Token
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * 3. Xác thực Token (kiểm tra tính hợp lệ và thời gian hết hạn)
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        // Kiểm tra Username có khớp và Token còn hạn không
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // --- Phương thức hỗ trợ ---

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    // Tạo Key từ Secret String
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}