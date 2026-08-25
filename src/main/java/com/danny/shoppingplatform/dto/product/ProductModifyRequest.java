package com.danny.shoppingplatform.dto.product;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
public class ProductModifyRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private MultipartFile photo;
}
