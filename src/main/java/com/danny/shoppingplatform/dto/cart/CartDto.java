package com.danny.shoppingplatform.dto.cart;

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
}
