package com.danny.shoppingplatform.dto.product;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductCreateRequest {
    private String name;
    private String description;
    private Integer price;
    private Integer quantity;
    private MultipartFile photo;
}
