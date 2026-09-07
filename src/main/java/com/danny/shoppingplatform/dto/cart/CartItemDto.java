package com.danny.shoppingplatform.dto.cart;

import com.danny.shoppingplatform.dto.product.ProductDto;
import com.danny.shoppingplatform.model.CartItem;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
public record CartItemDto(
        Long id,
        Integer quantity,
        Long memberId,
        ProductDto product,
        BigDecimal subtotal,
        Instant createdDate
) {
    public static CartItemDto fromEntity(CartItem cartItem) {
        if (cartItem == null) return null;

        BigDecimal price = (cartItem.getProduct() != null && cartItem.getProduct().getPrice() != null)
                ? cartItem.getProduct().getPrice()
                : BigDecimal.ZERO;
        BigDecimal quantity = BigDecimal.valueOf(cartItem.getQuantity() != null ? cartItem.getQuantity() : 0);

        return CartItemDto.builder()
                .id(cartItem.getId())
                .quantity(cartItem.getQuantity())
                .memberId(cartItem.getMember() != null ? cartItem.getMember().getId() : null)
                .product(ProductDto.fromEntity(cartItem.getProduct()))
                .subtotal(price.multiply(quantity))
                .createdDate(cartItem.getCreatedDate())
                .build();
    }
}
