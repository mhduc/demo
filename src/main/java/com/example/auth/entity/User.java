package com.example.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data // Lombok: Tự động tạo Getters, Setters, toString, equals/hashCode
@Builder // Lombok: Cho phép dùng cú pháp Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // Lưu trữ mật khẩu đã được BCrypt mã hóa

    private String email;

    private String fullname;
    
    // 🏷️ Trường Role
    @Enumerated(EnumType.STRING)
    private Role role; // Ví dụ: USER, ADMIN

    // --- Triển khai UserDetails interface ---

    // 1. Cung cấp Authorities/Roles của người dùng
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = (role == null) ? "UNKNOWN" : role.name();
        return List.of(new SimpleGrantedAuthority("ROLE_" + roleName));
    }

    // 2. Tên đăng nhập
    @Override
    public String getUsername() {
        return username;
    }

    // 3. Mật khẩu
    @Override
    public String getPassword() {
        return password;
    }
    
    // 4. Các phương thức kiểm tra trạng thái tài khoản (nên giữ mặc định là true)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}