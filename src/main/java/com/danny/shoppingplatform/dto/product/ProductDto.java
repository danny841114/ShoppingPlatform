package com.danny.shoppingplatform.dto.product;

import com.danny.shoppingplatform.model.Product;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
public record ProductDto(
        Long id,
        String name,
        Vendor vendor,
        String description,
        BigDecimal price,
        Integer quantity,
        Instant date
) {
    @Builder
    public record Vendor(
            Long id,
            String shopName
    ) {
    }

    public static ProductDto fromEntity(Product product, Long vendorId, String shopName) {
        if (product == null) return null;

        Vendor vendor = Vendor.builder()
                .id(vendorId)
                .shopName(shopName)
                .build();

        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .vendor(vendor)
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .date(product.getDate())
                .build();
    }

    public static ProductDto fromEntity(Product product) {
        if (product == null) return null;

        Long vendorId = product.getVendor() != null ? product.getVendor().getId() : null;
        String shopName = product.getVendor() != null ? product.getVendor().getShopName() : null;

        Vendor vendor = Vendor.builder()
                .id(vendorId)
                .shopName(shopName)
                .build();

        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .vendor(vendor)
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .date(product.getDate())
                .build();
    }
}