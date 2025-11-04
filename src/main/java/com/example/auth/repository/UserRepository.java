package com.example.auth.repository;

import com.example.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// UserRepository kế thừa JpaRepository để có các phương thức CRUD
public interface UserRepository extends JpaRepository<User, Long> {

    // Phương thức tùy chỉnh: Tìm kiếm User bằng Username
    Optional<User> findByUsername(String username);

    // Phương thức tùy chỉnh: Tìm kiếm User bằng Email
    Optional<User> findByEmail(String email);

    // Phương thức kiểm tra Username đã tồn tại chưa
    Boolean existsByUsername(String username);

    // Phương thức kiểm tra Email đã tồn tại chưa (tùy chọn)
    Boolean existsByEmail(String email);
}