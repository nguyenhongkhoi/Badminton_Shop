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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // phan quyen
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        //public path
                        .requestMatchers("/", "/home", "/register", "/login", "/products/**", "/css/**", "/js/**", "/images/**").permitAll()

                        // phai reget
                        .requestMatchers("/cart/**", "/checkout", "/orders/**").hasAnyAuthority("USER", "ADMIN")

                        // ad
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")

                        // rest
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") //cho Fe(theamleaf)
                        .loginProcessingUrl("/perform_login")
                        .defaultSuccessUrl("/", true)//ok -> trang chu
                        .failureUrl("/login?error=true") //error
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") //logout
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true) // Xóa session
                        .deleteCookies("JSESSIONID") // xoa all cookie
                        .permitAll()
                );

        return http.build();
    }
}