package com.badminton.shop.repository;

import com.badminton.shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Tìm user theo username (Dùng cho đăng nhập)
    Optional<User> findByUsername(String username);

    // Kiểm tra tồn tại để validate khi đăng ký
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}