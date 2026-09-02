package com.danny.shoppingplatform.dto.product;

import com.danny.shoppingplatform.model.Product;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private Vendor vendor;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private Instant date;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Vendor {
        private Long id;
        private String shopName;
    }

    public static ProductDto fromEntity(Product product, Long vendorId, String shopName) {
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
        Vendor vendor = Vendor.builder()
                .id(product.getVendor().getId())
                .shopName(product.getVendor().getShopName())
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