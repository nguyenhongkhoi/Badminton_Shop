package com.badminton.shop.repository;

import com.badminton.shop.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // Kế thừa các hàm cơ bản: findAll(), findById(), save(), delete()...
}