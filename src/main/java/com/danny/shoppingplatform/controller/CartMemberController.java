package com.danny.shoppingplatform.controller;

import com.danny.shoppingplatform.annotation.CurrentAccount;
import com.danny.shoppingplatform.dto.cart.AddCartItemRequest;
import com.danny.shoppingplatform.dto.cart.CartItemDto;
import com.danny.shoppingplatform.dto.cart.UpdateCartItemRequest;
import com.danny.shoppingplatform.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member/cart/items")
public class CartMemberController {
    private final CartService cartService;

    @GetMapping
    private ResponseEntity<List<CartItemDto>> getCartItems(@CurrentAccount String account) {
        List<CartItemDto> cartItemDtos = cartService.getCartItems(account);
        return ResponseEntity.ok(cartItemDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCartItem(@PathVariable Long id,
                                               @RequestBody UpdateCartItemRequest request,
                                               @CurrentAccount String account) {
        cartService.updateCartItem(id, request, account);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long id, @CurrentAccount String account) {
        cartService.removeCartItem(id, account);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<CartItemDto> addCartItem(@RequestBody AddCartItemRequest request,
                                                   @CurrentAccount String account) {
        CartItemDto cartItemDto = cartService.addCartItem(request, account);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto); // TODO: need to fix
    }
}