package com.badminton.shop.repository;

import com.badminton.shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    // Lấy danh sách sản phẩm theo ID của Category (Phục vụ chức năng Lọc theo danh mục)
    List<Product> findByCategoryId(Integer categoryId);

    // Lấy danh sách sản phẩm theo ID của Brand (Phục vụ chức năng Lọc theo thương hiệu)
    List<Product> findByBrandId(Integer brandId);
}