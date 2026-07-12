package com.danny.shoppingplatform.dto.cart;

import com.danny.shoppingplatform.model.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private Integer id;
    private Integer quantity;
    private Integer memberId;
    private Integer productId;
    private Instant createdDate;

    public static CartDto fromEntity(Cart cart) {
        if (cart == null) return null;

        return CartDto.builder()
                .id(cart.getId())
                .quantity(cart.getQuantity())
                .memberId(cart.getMember().getId())
                .productId(cart.getProduct().getId())
                .createdDate(cart.getCreatedDate())
                .build();
    }
}
