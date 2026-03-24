package com.badminton.shop.controller;

import com.badminton.shop.entity.User;
import com.badminton.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // Hiển thị form đăng nhập
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Trả về file login.html
    }

    // Hiển thị form đăng ký
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User()); // Truyền đối tượng rỗng để map với form
        return "register"; // Trả về file register.html
    }

    // Xử lý khi người dùng submit form đăng ký
    @PostMapping("/register")
    public String processRegister(@ModelAttribute("user") User user, Model model) {
        try {
            userService.registerUser(user);
            // Thành công -> Chuyển hướng về trang đăng nhập kèm thông báo
            return "redirect:/login?registered=true";
        } catch (RuntimeException e) {
            // Lỗi (trùng email/username) -> Ở lại trang đăng ký và hiện lỗi
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}