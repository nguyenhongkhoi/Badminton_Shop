package com.badminton.shop.repository;

import com.badminton.shop.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
    // Kế thừa các hàm cơ bản: findAll(), findById(), save(), delete()...
}
