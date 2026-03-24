package com.badminton.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Khai báo Bean mã hóa mật khẩu BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Cấu hình phân quyền và luồng Đăng nhập/Đăng xuất
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Tạm tắt CSRF để dễ test, thực tế khi deploy nên bật và cấu hình token
                .authorizeHttpRequests(auth -> auth
                        // Các trang ai cũng xem được (Trang chủ, sản phẩm, đăng ký, đăng nhập và các file tĩnh CSS/JS/Image)
                        .requestMatchers("/", "/home", "/register", "/login", "/products/**", "/css/**", "/js/**", "/images/**").permitAll()

                        // Các trang yêu cầu phải đăng nhập (Giỏ hàng, thanh toán, lịch sử đơn hàng)
                        .requestMatchers("/cart/**", "/checkout", "/orders/**").hasAnyAuthority("USER", "ADMIN")

                        // Các trang chỉ dành cho Admin
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")

                        // Tất cả các request khác đều cần xác thực
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // Custom lại trang đăng nhập bằng Thymeleaf (sẽ tạo ở frontend)
                        .loginProcessingUrl("/perform_login") // URL khi submit form đăng nhập
                        .defaultSuccessUrl("/", true) // Đăng nhập thành công về trang chủ
                        .failureUrl("/login?error=true") // Đăng nhập sai trả về tham số error
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL gọi khi muốn đăng xuất
                        .logoutSuccessUrl("/") // Đăng xuất xong về trang chủ
                        .invalidateHttpSession(true) // Xóa session
                        .deleteCookies("JSESSIONID") // Xóa cookie
                        .permitAll()
                );

        return http.build();
    }
}