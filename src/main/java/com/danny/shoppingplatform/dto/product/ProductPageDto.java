package com.danny.shoppingplatform.dto.product;

import com.danny.shoppingplatform.model.Product;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPageDto {
    private List<ProductDto> products;
    private Integer totalPages;
    private Long totalElements;
    private Integer page;
    private Integer size;
    private String keyword;

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
