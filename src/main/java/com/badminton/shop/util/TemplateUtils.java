package com.badminton.shop.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Component("templateUtils")
public class TemplateUtils {

    private static final Locale VIETNAMESE = Locale.forLanguageTag("vi-VN");

    public String resolveImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return "/images/placeholder-product.svg";
        }

        if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://") || imageUrl.startsWith("/")) {
            return imageUrl;
        }

        return "/images/" + imageUrl;
    }

    public String formatCurrency(BigDecimal amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(VIETNAMESE);
        formatter.setMaximumFractionDigits(0);
        formatter.setMinimumFractionDigits(0);
        return formatter.format(amount == null ? BigDecimal.ZERO : amount);
    }

    public String shortDescription(String description, int maxLength) {
        if (description == null || description.isBlank()) {
            return "Thiet ke toi uu cho nguoi choi cau long can cam giac danh on dinh va hieu suat cao.";
        }

        String sanitized = description.replaceAll("\\s+", " ").trim();
        if (sanitized.length() <= maxLength) {
            return sanitized;
        }

        return sanitized.substring(0, Math.max(0, maxLength - 3)) + "...";
    }
}
