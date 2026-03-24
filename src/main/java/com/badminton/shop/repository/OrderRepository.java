package com.badminton.shop.repository;

import com.badminton.shop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    // Lấy danh sách đơn hàng của một User (Dùng cho chức năng Xem lịch sử mua hàng)
    List<Order> findByUserId(Integer userId);
}