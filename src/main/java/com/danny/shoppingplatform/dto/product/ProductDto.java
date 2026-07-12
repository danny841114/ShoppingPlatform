package com.danny.shoppingplatform.dto.product;

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
}