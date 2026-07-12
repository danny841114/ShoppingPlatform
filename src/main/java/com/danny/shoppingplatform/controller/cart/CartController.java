package com.danny.shoppingplatform.controller.cart;

import com.danny.shoppingplatform.model.Cart;
import com.danny.shoppingplatform.service.CartService;
import com.danny.shoppingplatform.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.HashMap;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private final MemberService memberService;

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
                                                @RequestBody HashMap<String, Integer> requestBody) throws AccountNotFoundException {
        if (memberService.getLoginMember() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("尚未登入");
        }

        int memberId = memberService.getLoginMember().getId();
        Integer quantity = requestBody.get("quantity");
        Cart cart = cartService.addProductIntoCart(memberId, productId, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }
}