package com.danny.shoppingplatform.controller.cart;

import com.danny.shoppingplatform.annotation.CurrentAccount;
import com.danny.shoppingplatform.dto.cart.CartAddRequest;
import com.danny.shoppingplatform.dto.cart.CartDto;
import com.danny.shoppingplatform.model.Cart;
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
    public ResponseEntity<?> deleteProductFromCart(@PathVariable Integer id) {
        cartService.deleteProductFromCart(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/quantity/increase")
    public ResponseEntity<?> increaseProductQuantity(@PathVariable Integer id) {
        Cart cart = cartService.increaseProductQuantity(id);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/{id}/quantity/decrease")
    public ResponseEntity<?> decreaseProductQuantity(@PathVariable Integer id) {
        Cart cart = cartService.decreaseProductQuantity(id);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/product/{productId}/add")
    public ResponseEntity<CartDto> addProductIntoCart(@PathVariable Integer productId,
                                                      @RequestBody CartAddRequest request,
                                                      @CurrentAccount String account) {
        CartDto cartDto = cartService.addProductIntoCart(productId, request, account);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartDto);
    }
}