package com.badminton.shop.controller;

import com.badminton.shop.entity.Product;
import com.badminton.shop.service.BrandService;
import com.badminton.shop.service.CategoryService;
import com.badminton.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;

    // Trang chủ & chức năng Lọc
    @GetMapping({"/", "/home"})
    public String home(
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "brandId", required = false) Integer brandId,
            Model model) {

        List<Product> products;

        // Logic lọc sản phẩm
        if (categoryId != null) {
            products = productService.getProductsByCategoryId(categoryId);
        } else if (brandId != null) {
            products = productService.getProductsByBrandId(brandId);
        } else {
            products = productService.getAllProducts();
        }

        // Đổ dữ liệu ra View (Thymeleaf)
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("brands", brandService.getAllBrands());

        return "index"; // Trả về file index.html
    }

    // Trang Chi tiết sản phẩm
    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Integer id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm này!"));

        model.addAttribute("product", product);
        return "product-detail"; // Trả về file product-detail.html
    }
}