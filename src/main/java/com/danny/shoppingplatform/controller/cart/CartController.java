package com.danny.shoppingplatform.controller.cart;

import com.danny.shoppingplatform.model.Cart;
import com.danny.shoppingplatform.service.CartService;
import com.danny.shoppingplatform.service.MemberService;
import com.danny.shoppingplatform.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

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
}
