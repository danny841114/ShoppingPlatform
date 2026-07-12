package com.danny.shoppingplatform.dto.product;

import com.danny.shoppingplatform.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPageDto {
    private List<Product> products;
    private Integer totalPages;
    private Long totalElements;
    private Integer page;
    private Integer size;
    private String keyword;

    public static ProductPageDto fromEntity(Page<Product> productPage, Pageable pageable) {
        if (productPage == null) return null;

        return ProductPageDto.builder()
                .products(productPage.getContent())
                .totalPages(productPage.getTotalPages())
                .totalElements(productPage.getTotalElements())
                .page(pageable.getPageNumber())
                .size(productPage.getSize())
                .build();
    }
}
