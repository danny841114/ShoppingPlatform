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
    private byte[] photo;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Vendor {
        private Long id;
        private String account;
    }

    public static ProductDto fromEntity(Product product, Long vendorId, String vendorAccount) {
        Vendor vendor = Vendor.builder()
                .id(vendorId)
                .account(vendorAccount)
                .build();

        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .vendor(vendor)
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .date(product.getDate())
                .photo(product.getPhoto())
                .build();
    }

    public static ProductDto fromEntity(Product product) {
        Vendor vendor = Vendor.builder()
                .id(product.getMember().getId())
                .account(product.getMember().getAccount())
                .build();

        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .vendor(vendor)
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .date(product.getDate())
                .photo(product.getPhoto())
                .build();
    }
}