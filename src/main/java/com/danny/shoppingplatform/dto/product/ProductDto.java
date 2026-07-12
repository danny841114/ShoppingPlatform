package com.danny.shoppingplatform.dto.product;

import com.danny.shoppingplatform.model.Product;
import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Integer id;
    private String name;
    private Integer vendorId;
    private String description;
    private Integer price;
    private Integer quantity;
    private Date date;
    private byte[] photo;

    public static ProductDto fromEntity(Product product, Integer vendorId) {
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