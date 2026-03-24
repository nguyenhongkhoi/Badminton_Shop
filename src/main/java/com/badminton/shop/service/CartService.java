package com.badminton.shop.service;

import com.badminton.shop.dto.CartItem;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
@SessionScope // Đảm bảo mỗi người dùng truy cập web có 1 giỏ hàng riêng biệt trong Session
public class CartService {

    // Dùng Map để dễ dàng thêm/sửa/xóa sản phẩm dựa vào productId
    private Map<Integer, CartItem> map = new HashMap<>();

    public void add(CartItem item) {
        // Nếu sản phẩm đã có trong giỏ -> tăng số lượng
        CartItem existedItem = map.get(item.getProductId());
        if (existedItem != null) {
            existedItem.setQuantity(existedItem.getQuantity() + item.getQuantity());
        } else {
            // Nếu chưa có -> thêm mới vào map
            map.put(item.getProductId(), item);
        }
    }

    public void remove(Integer productId) {
        map.remove(productId);
    }

    public void update(Integer productId, int quantity) {
        CartItem item = map.get(productId);
        if (item != null) {
            if (quantity <= 0) {
                map.remove(productId);
            } else {
                item.setQuantity(quantity);
            }
        }
    }

    public void clear() {
        map.clear();
    }

    public Collection<CartItem> getItems() {
        return map.values();
    }

    public int getCount() {
        return map.values().stream().mapToInt(CartItem::getQuantity).sum();
    }

    public BigDecimal getTotalAmount() {
        return map.values().stream()
                .map(CartItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}