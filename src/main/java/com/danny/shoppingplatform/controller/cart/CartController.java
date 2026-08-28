package com.danny.shoppingplatform.controller.cart;

import com.danny.shoppingplatform.annotation.CurrentAccount;
import com.danny.shoppingplatform.dto.cart.CartAddRequest;
import com.danny.shoppingplatform.dto.cart.CartDto;
import com.danny.shoppingplatform.dto.cart.CartUpdateRequest;
import com.danny.shoppingplatform.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    @GetMapping
    private ResponseEntity<List<CartDto>> getCartItems(@CurrentAccount String account) {
        List<CartDto> cartDtos = cartService.getCartItems(account);
        return ResponseEntity.ok(cartDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCartItem(@PathVariable Long id,
                                               @RequestBody CartUpdateRequest request,
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
    public ResponseEntity<CartDto> addCartItem(@RequestBody CartAddRequest request,
                                               @CurrentAccount String account) {
        cartService.addCartItem(request, account);
        return ResponseEntity.status(HttpStatus.CREATED).build(); // TODO: need to fix
    }
}