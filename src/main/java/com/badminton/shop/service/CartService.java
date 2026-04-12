package com.badminton.shop.service;

import com.badminton.shop.dto.CartItem;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
@SessionScope
public class CartService {

    private Map<Integer, CartItem> map = new HashMap<>();

    public void add(CartItem item) {
        // update số lượng
        CartItem existedItem = map.get(item.getProductId());
        if (existedItem != null) {
            existedItem.setQuantity(existedItem.getQuantity() + item.getQuantity());
        } else {
            // update map
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