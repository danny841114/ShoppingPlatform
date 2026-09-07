package com.danny.shoppingplatform.dto.cart;

public record AddCartItemRequest(Long productId, Integer quantity) {
}
