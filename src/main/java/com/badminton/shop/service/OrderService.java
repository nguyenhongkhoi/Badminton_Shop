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

    // @Transactional đảm bảo: Nếu lưu lỗi ở bất kỳ bước nào (ví dụ hết hàng), toàn bộ quá trình bị hủy (Rollback), không lưu dữ liệu rác vào DB
    @Transactional
    public void placeOrder(User user, CartService cartService, String shippingAddress) {
        if (cartService.getItems().isEmpty()) {
            throw new RuntimeException("Giỏ hàng đang trống!");
        }

        // 1. Tạo và lưu thông tin Đơn Hàng (Order)
        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(shippingAddress);
        order.setStatus("PENDING");
        order.setTotalAmount(cartService.getTotalAmount());

        Order savedOrder = orderRepository.save(order);

        // 2. Duyệt qua giỏ hàng để lưu Chi tiết Đơn Hàng (OrderDetail) và Trừ Tồn Kho (Stock)
        for (CartItem item : cartService.getItems()) {
            // Lấy sản phẩm từ DB để kiểm tra tồn kho
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm ID: " + item.getProductId()));

            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Sản phẩm '" + product.getName() + "' không đủ số lượng tồn kho!");
            }

            // Trừ tồn kho và cập nhật lại sản phẩm
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);

            // Lưu OrderDetail
            OrderDetail detail = new OrderDetail();
            detail.setOrder(savedOrder);
            detail.setProduct(product);
            detail.setPrice(item.getPrice()); // Lưu lại giá tại thời điểm mua
            detail.setQuantity(item.getQuantity());

            orderDetailRepository.save(detail);
        }

        // 3. Xóa sạch giỏ hàng sau khi đặt thành công
        cartService.clear();
    }

    // Lấy danh sách lịch sử mua hàng của User
    public List<Order> getOrderHistory(Integer userId) {
        return orderRepository.findByUserId(userId);
    }
}