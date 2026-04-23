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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;

    @GetMapping({"/", "/home"})
    public String home(
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "brandId", required = false) Integer brandId,
            Model model) {

        List<Product> allProducts = productService.getAllProducts();
        List<Product> products;

        if (categoryId != null) {
            products = productService.getProductsByCategoryId(categoryId);
        } else if (brandId != null) {
            products = productService.getProductsByBrandId(brandId);
        } else {
            products = allProducts;
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("selectedBrandId", brandId);
        model.addAttribute("heroProduct", products.isEmpty() ? null : products.get(0));
        model.addAttribute("featuredProducts", products.stream().limit(8).toList());
        model.addAttribute("newArrivalProducts", allProducts.stream()
                .sorted(Comparator.comparing(Product::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(4)
                .toList());

        return "main_page";
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Integer id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Khong tim thay san pham nay"));

        List<Product> allProducts = productService.getAllProducts();
        List<Product> relatedProducts = allProducts.stream()
                .filter(item -> !item.getId().equals(product.getId()))
                .filter(item -> product.getCategory() == null
                        || item.getCategory() == null
                        || item.getCategory().getId().equals(product.getCategory().getId()))
                .limit(4)
                .collect(Collectors.toCollection(ArrayList::new));

        if (relatedProducts.size() < 4) {
            List<Integer> usedIds = relatedProducts.stream()
                    .map(Product::getId)
                    .collect(Collectors.toCollection(ArrayList::new));
            usedIds.add(product.getId());

            relatedProducts.addAll(allProducts.stream()
                    .filter(item -> !usedIds.contains(item.getId()))
                    .limit(4 - relatedProducts.size())
                    .toList());
        }

        model.addAttribute("product", product);
        model.addAttribute("relatedProducts", relatedProducts);
        return "detail_page";
    }
}
