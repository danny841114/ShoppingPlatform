package com.danny.shoppingplatform.dto.product;

import com.danny.shoppingplatform.model.Product;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record ProductPageDto(
        List<ProductDto> products,
        Integer totalPages,
        Long totalElements,
        Integer page,
        Integer size,
        String keyword
) {
    public static ProductPageDto fromEntity(Page<Product> productPage) {
        if (productPage == null) return null;

        List<ProductDto> products = productPage.getContent()
                .stream()
                .map(ProductDto::fromEntity)
                .toList();

        return ProductPageDto.builder()
                .products(products)
                .totalPages(productPage.getTotalPages())
                .totalElements(productPage.getTotalElements())
                .page(productPage.getNumber())
                .size(productPage.getSize())
                .build();
    }
}
