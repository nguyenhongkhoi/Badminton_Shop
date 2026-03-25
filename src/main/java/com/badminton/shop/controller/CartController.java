package com.badminton.shop.controller;

import com.badminton.shop.dto.CartItem;
import com.badminton.shop.entity.Product;
import com.badminton.shop.service.CartService;
import com.badminton.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final ProductService productService;

    // Xem giỏ hàng
    @GetMapping
    public String viewCart(Model model) {
        model.addAttribute("cartItems", cartService.getItems());
        model.addAttribute("totalAmount", cartService.getTotalAmount());
        return "cart"; // Trả về file cart.html
    }

    // Bấm nút "Thêm vào giỏ"
    @PostMapping("/add")
    public String addToCart(@RequestParam Integer productId, @RequestParam(defaultValue = "1") int quantity) {
        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        CartItem item = new CartItem(
                product.getId(),
                product.getName(),
                product.getImageUrl(),
                product.getPrice(),
                quantity
        );
        cartService.add(item);

        return "redirect:/cart"; // Thêm xong tự động chuyển sang trang giỏ hàng
    }

    // Cập nhật số lượng
    @PostMapping("/update")
    public String updateCart(@RequestParam Integer productId, @RequestParam int quantity) {
        cartService.update(productId, quantity);
        return "redirect:/cart";
    }

    // Xóa 1 sản phẩm
    @GetMapping("/remove/{productId}")
    public String removeCartItem(@PathVariable Integer productId) {
        cartService.remove(productId);
        return "redirect:/cart";
    }

    // Xóa sạch giỏ
    @GetMapping("/clear")
    public String clearCart() {
        cartService.clear();
        return "redirect:/cart";
    }

}