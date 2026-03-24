package com.badminton.shop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Cấu hình map đường dẫn URL /css/**, /js/**, /images/** vào các thư mục vật lý tương ứng trong static
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");

        // (Mở rộng sau này) Nếu bạn muốn lưu ảnh upload bên ngoài máy tính (ví dụ ổ D:) thì cấu hình tại đây:
        // registry.addResourceHandler("/uploads/**").addResourceLocations("file:///D:/badminton-shop-uploads/");
    }
}