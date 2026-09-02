package com.danny.shoppingplatform.service;

import com.danny.shoppingplatform.dto.cart.CartAddRequest;
import com.danny.shoppingplatform.dto.cart.CartDto;
import com.danny.shoppingplatform.dto.cart.CartUpdateRequest;
import com.danny.shoppingplatform.model.Cart;
import com.danny.shoppingplatform.model.Member;
import com.danny.shoppingplatform.model.Product;
import com.danny.shoppingplatform.repository.CartRepository;
import com.danny.shoppingplatform.repository.MemberRepository;
import com.danny.shoppingplatform.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class CartService {
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public List<CartDto> getCartItems(String account) {
        if (!memberRepository.existsByUserAccount(account)) {
            throw new UsernameNotFoundException("Member with account '%s' not found".formatted(account));
        }

        return cartRepository.findByMemberUserAccount(account)
                .stream()
                .map(CartDto::fromEntity)
                .toList();
    }

    @Transactional
    public void updateCartItem(Long cartId, CartUpdateRequest request, String account) {
        Cart cart = getCartItemById(cartId);
        Member member = getMemberByAccount(account);
        if (!Objects.equals(member.getId(), cart.getMember().getId())) {
            throw new AccessDeniedException("Cart owner and member does not match");
        }

        Integer productQuantity = cart.getProduct().getQuantity();
        Integer requestQuantity = request.getQuantity();
        if (requestQuantity > productQuantity || requestQuantity < 1) {
            throw new ArithmeticException("Request quantity is illegal");
        }

        cart.setQuantity(requestQuantity);
        cartRepository.save(cart);
    }

    @Transactional
    public void removeCartItem(Long cartId, String account) {
        Cart cart = getCartItemById(cartId);
        Member member = getMemberByAccount(account);
        if (!Objects.equals(member.getId(), cart.getMember().getId())) {
            throw new AccessDeniedException("Cart owner and member does not match");
        }

        cartRepository.delete(cart);
    }

    @Transactional
    public CartDto addCartItem(CartAddRequest request, String account) {
        Integer inputQuantity = request.getQuantity();
        if (inputQuantity == null || inputQuantity <= 0) {
            throw new IllegalArgumentException("Quantity must be more than 0");
        }

        Member member = getMemberByAccount(account);

        Long productId = request.getProductId();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product with ID '%s' not found".formatted(productId)));

        Cart cart = cartRepository.findByMemberAndProduct(member, product)
                .orElseGet(() -> Cart.create(member, product, 0));

        int currentQuantity = cart.getQuantity();
        int availableStock = product.getQuantity();
        int remainingAllowed = availableStock - currentQuantity;

        // 情境 A：購物車原本就已經放滿庫存
        if (remainingAllowed <= 0) {
            throw new IllegalArgumentException(
                    "The limit for this item in your cart has been reached (Maximum: %d)".formatted(availableStock)
            );
        }

        // 情境 B：欲新增數量超過剩餘可加購額度
        if (inputQuantity > remainingAllowed) {
            throw new IllegalArgumentException(
                    "Insufficient stock. You can only add up to %d more of this item (Cart: %d, Stock: %d)"
                            .formatted(remainingAllowed, currentQuantity, availableStock)
            );
        }

        cart.setQuantity(currentQuantity + inputQuantity);

        Cart savedItem = cartRepository.save(cart);
        return CartDto.fromEntity(savedItem);
    }

    private Cart getCartItemById(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item with ID '%s' not found".formatted(cartId)));
    }

    private Member getMemberByAccount(String account) {
        return memberRepository.findByUserAccount(account)
                .orElseThrow(() -> new EntityNotFoundException("Member with account '%s' not found".formatted(account)));
    }
}