package com.danny.shoppingplatform.dto.cart;

import com.danny.shoppingplatform.dto.product.ProductDto;
import com.danny.shoppingplatform.model.CartItem;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private Long id;
    private Integer quantity;
    private Long memberId;
    private ProductDto product;
    private BigDecimal subtotal;
    private Instant createdDate;

    public static CartItemDto fromEntity(CartItem cartItem) {
        if (cartItem == null) return null;

        BigDecimal price = cartItem.getProduct() != null ? cartItem.getProduct().getPrice() : BigDecimal.ZERO;
        BigDecimal quantity = BigDecimal.valueOf(cartItem.getQuantity());

        return CartItemDto.builder()
                .id(cartItem.getId())
                .quantity(cartItem.getQuantity())
                .memberId(cartItem.getMember().getId())
                .product(ProductDto.fromEntity(cartItem.getProduct()))
                .subtotal(price.multiply(quantity))
                .createdDate(cartItem.getCreatedDate())
                .build();
    }
}
