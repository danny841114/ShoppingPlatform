package com.danny.shoppingplatform.dto.product;

import com.danny.shoppingplatform.model.Product;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private Long vendorId;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private Instant date;
    private byte[] photo;

    public static ProductDto fromEntity(Product product, Long vendorId) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .vendorId(vendorId)
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .date(product.getDate())
                .photo(product.getPhoto())
                .build();
    }
}