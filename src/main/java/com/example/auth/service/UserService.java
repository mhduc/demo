package com.example.auth.service;

import com.example.auth.entity.Role;
import com.example.auth.entity.User;
import com.example.auth.dto.ChangePasswordRequest;
import com.example.auth.dto.RegisterRequest;
import com.example.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // BCryptPasswordEncoder

    /**
     * 1. Logic Đăng ký tài khoản mới (Register)
     */
    public User registerNewUser(RegisterRequest request) {
        // Kiểm tra Username đã tồn tại
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username đã được sử dụng!"); 
        }
        
        // Tạo đối tượng User mới
        User user = User.builder()
                .username(request.getUsername())
                // 🔒 Mã hóa mật khẩu trước khi lưu
                .password(passwordEncoder.encode(request.getPassword())) 
                .username(request.getUsername())
                .email(request.getEmail())    
                // Gán Role mặc định
                .role(Role.USER) 
                .build();

        return userRepository.save(user);
    }

    /**
     * 2. Logic Đổi mật khẩu (Change Password)
     */
    public void changePassword(String username, ChangePasswordRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại!"));

        // 1. Xác thực mật khẩu cũ (Current Password)
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Mật khẩu hiện tại không chính xác!");
        }

        // 2. Mã hóa và cập nhật mật khẩu mới
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
    
    /**
     * 3. Logic lấy danh sách User (chỉ dành cho ADMIN)
     */
     // Bạn có thể thêm logic phân trang/tìm kiếm ở đây
     // public Page<User> listAllUsers(Pageable pageable) { ... }
}