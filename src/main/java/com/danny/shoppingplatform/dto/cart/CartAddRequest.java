package com.danny.shoppingplatform.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartAddRequest {
    private Long productId;
    private Integer quantity;
}
