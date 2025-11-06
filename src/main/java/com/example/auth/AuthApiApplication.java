package com.example.auth;

import com.example.auth.entity.Role;
import com.example.auth.entity.User;
import com.example.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AuthApiApplication {

    public static void main(String[] args) {
        // Phương thức này khởi chạy toàn bộ ứng dụng Spring Boot
        SpringApplication.run(AuthApiApplication.class, args);
    }
    
    // --- Khởi tạo dữ liệu mẫu (Seeder) ---
    
    /**
     * @Bean CommandLineRunner: Chạy một lần duy nhất khi ứng dụng khởi động thành công.
     * Dùng để tạo tài khoản Admin đầu tiên nếu chưa tồn tại.
     */
    @Bean
    public CommandLineRunner run(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Kiểm tra xem đã có tài khoản admin chưa
            if (userRepository.findByUsername("admin").isEmpty()) {
                
                // 1. Tạo User Admin
                User admin = User.builder()
                        .username("admin")
                        .email("admin@example.com")
                        // 🔒 Mã hóa mật khẩu
                        .password(passwordEncoder.encode("admin123")) 
                        // 🏷️ Gán Role ADMIN
                        .role(Role.ADMIN)
                        .build();

                // 2. Lưu vào Database
                userRepository.save(admin);
                System.out.println("✅ Tài khoản Admin mặc định đã được tạo: admin / admin123");
            }
            
            // Tạo User thường để kiểm thử phân quyền
            if (userRepository.findByUsername("testuser").isEmpty()) {
                User user = User.builder()
                        .username("testuser")
                        .email("testuser@example.com")
                        .password(passwordEncoder.encode("user123")) 
                        .role(Role.USER) 
                        .build();

                userRepository.save(user);
                System.out.println("✅ Tài khoản User mặc định đã được tạo: testuser / user123");
            }
        };
    }
}