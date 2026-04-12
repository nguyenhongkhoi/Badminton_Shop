package com.badminton.shop.service;

import com.badminton.shop.dto.CartItem;
import com.badminton.shop.entity.Order;
import com.badminton.shop.entity.OrderDetail;
import com.badminton.shop.entity.Product;
import com.badminton.shop.entity.User;
import com.badminton.shop.repository.OrderDetailRepository;
import com.badminton.shop.repository.OrderRepository;
import com.badminton.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void placeOrder(User user, CartService cartService, String shippingAddress) {
        if (cartService.getItems().isEmpty()) {
            throw new RuntimeException("Giỏ hàng đang trống!");
        }

       // save ttin don hang
        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(shippingAddress);
        order.setStatus("PENDING");
        order.setTotalAmount(cartService.getTotalAmount());

        Order savedOrder = orderRepository.save(order);

        //duyet luu vi tri thay doi
        for (CartItem item : cartService.getItems()) {
            // Lấy sản phẩm từ DB để kiểm tra tồn kho
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm ID: " + item.getProductId()));

            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Sản phẩm '" + product.getName() + "' không đủ số lượng tồn kho!");
            }

            //update sản phẩm
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);

            // Lưu OrderDetail
            OrderDetail detail = new OrderDetail();
            detail.setOrder(savedOrder);
            detail.setProduct(product);
            detail.setPrice(item.getPrice()); // gia cuoi
            detail.setQuantity(item.getQuantity());

            orderDetailRepository.save(detail);
        }

        //xoa gio do day
        cartService.clear();
    }

    // lsu mua
    public List<Order> getOrderHistory(Integer userId) {
        return orderRepository.findByUserId(userId);
    }
}