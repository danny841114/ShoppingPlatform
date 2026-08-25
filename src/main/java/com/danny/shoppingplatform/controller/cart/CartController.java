package com.danny.shoppingplatform.controller.cart;

import com.danny.shoppingplatform.annotation.CurrentAccount;
import com.danny.shoppingplatform.dto.cart.CartAddRequest;
import com.danny.shoppingplatform.dto.cart.CartDto;
import com.danny.shoppingplatform.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long id, @CurrentAccount String account) {
        cartService.removeCartItem(id, account);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/quantity/increase")
    public ResponseEntity<CartDto> increaseProductQuantity(@PathVariable Long id, @CurrentAccount String account) {
        CartDto cartDto = cartService.increaseProductQuantity(id, account);
        return ResponseEntity.ok(cartDto);
    }

    @PutMapping("/{id}/quantity/decrease")
    public ResponseEntity<CartDto> decreaseProductQuantity(@PathVariable Long id, @CurrentAccount String account) {
        CartDto cartDto = cartService.decreaseProductQuantity(id, account);
        return ResponseEntity.ok(cartDto);
    }

    @PostMapping("/product/{productId}/add")
    public ResponseEntity<CartDto> addCartItem(@PathVariable Long productId,
                                               @RequestBody CartAddRequest request,
                                               @CurrentAccount String account) {
        CartDto cartDto = cartService.addCartItem(productId, request, account);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartDto);
    }
}