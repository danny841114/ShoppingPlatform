package com.danny.shoppingplatform.dto.cart;

import com.danny.shoppingplatform.dto.product.ProductDto;
import com.danny.shoppingplatform.model.Cart;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private Long id;
    private Integer quantity;
    private Long memberId;
    private ProductDto product;
    private BigDecimal subtotal;
    private Instant createdDate;

    public static CartDto fromEntity(Cart cart) {
        if (cart == null) return null;

        BigDecimal price = cart.getProduct() != null ? cart.getProduct().getPrice() : BigDecimal.ZERO;
        BigDecimal quantity = BigDecimal.valueOf(cart.getQuantity());

        return CartDto.builder()
                .id(cart.getId())
                .quantity(cart.getQuantity())
                .memberId(cart.getMember().getId())
                .product(ProductDto.fromEntity(cart.getProduct()))
                .subtotal(price.multiply(quantity))
                .createdDate(cart.getCreatedDate())
                .build();
    }
}
