package com.danny.shoppingplatform.dto.product;

import com.danny.shoppingplatform.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
