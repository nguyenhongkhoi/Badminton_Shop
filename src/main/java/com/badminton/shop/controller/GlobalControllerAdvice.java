package com.badminton.shop.controller;

import com.badminton.shop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final CartService cartService;

    // Biến "cartCount" sẽ tự động được gửi xuống mọi file Thymeleaf HTML
    @ModelAttribute("cartCount")
    public int getCartCount() {
        return cartService.getCount();
    }
}