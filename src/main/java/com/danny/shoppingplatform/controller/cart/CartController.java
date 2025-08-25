package com.danny.shoppingplatform.controller.cart;

import com.danny.shoppingplatform.model.Cart;
import com.danny.shoppingplatform.repository.CartRepository;
import com.danny.shoppingplatform.service.CartService;
import com.danny.shoppingplatform.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private final MemberService memberService;

    public CartController(CartService cartService, MemberService memberService) {
        this.cartService = cartService;
        this.memberService = memberService;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public Map<String, String> handleIllegalArgument(IllegalArgumentException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public Map<String, String> handleNotFound(EntityNotFoundException ex) {
        return Map.of("error", ex.getMessage());
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

    @PostMapping("/product/{productId}/add")
    public ResponseEntity<?> addProductIntoCart(@PathVariable Integer productId,
                                                @RequestBody HashMap<String, Integer> requestBody) {
        int memberId = memberService.getLoginMember().getId();
        Integer quantity = requestBody.get("quantity");
        Cart cart = cartService.addProductIntoCart(memberId, productId, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }
}
