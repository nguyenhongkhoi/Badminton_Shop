package com.badminton.shop.repository;

import com.badminton.shop.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    // Lấy danh sách chi tiết của một đơn hàng cụ thể
    List<OrderDetail> findByOrderId(Integer orderId);
}