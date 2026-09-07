package com.danny.shoppingplatform.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public record AddProductRequest(
        @NotBlank(message = "Product name should not be null") String name,
        String description,
        @NotNull(message = "Product price should not be null") @Min(value = 0, message = "價格不可小於 0") BigDecimal price,
        @NotNull(message = "Product quantity should not be null") @Min(value = 0, message = "數量不可小於 0") Integer quantity,
        MultipartFile photo
) {
}
