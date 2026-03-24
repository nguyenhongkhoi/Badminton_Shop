package com.badminton.shop.controller;

import com.badminton.shop.entity.User;
import com.badminton.shop.service.CartService;
import com.badminton.shop.service.OrderService;
import com.badminton.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final CartService cartService;
    private final OrderService orderService;
    private final UserService userService;

    // Hiển thị trang Thanh Toán (Chỉ vào được khi đã đăng nhập)
    @GetMapping("/checkout")
    public String checkoutPage(Model model, Principal principal) {
        if (cartService.getItems().isEmpty()) {
            return "redirect:/cart"; // Giỏ rỗng thì quay về giỏ hàng
        }

        // Truyền thông tin user để tự động điền địa chỉ giao hàng
        User user = userService.findByUsername(principal.getName()).get();
        model.addAttribute("user", user);

        model.addAttribute("cartItems", cartService.getItems());
        model.addAttribute("totalAmount", cartService.getTotalAmount());

        return "checkout"; // Trả về file checkout.html
    }

    // Xác nhận Đặt hàng
    @PostMapping("/checkout")
    public String processCheckout(@RequestParam String shippingAddress, Principal principal, Model model) {
        try {
            User user = userService.findByUsername(principal.getName()).get();

            // Gọi hàm Transactional đặt hàng (lưu Order, OrderDetail, trừ tồn kho)
            orderService.placeOrder(user, cartService, shippingAddress);

            return "redirect:/orders?success=true"; // Đặt thành công qua trang Lịch sử
        } catch (RuntimeException e) {
            // Nếu lỗi (ví dụ hết hàng)
            model.addAttribute("error", e.getMessage());
            model.addAttribute("cartItems", cartService.getItems());
            model.addAttribute("totalAmount", cartService.getTotalAmount());
            return "checkout";
        }
    }

    // Xem lịch sử mua hàng
    @GetMapping("/orders")
    public String orderHistory(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName()).get();
        model.addAttribute("orders", orderService.getOrderHistory(user.getId()));
        return "order-history"; // Trả về file order-history.html
    }
}