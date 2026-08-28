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

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import static com.danny.shoppingplatform.dto.cart.CartDto.fromEntity;

@RequiredArgsConstructor
@Service
public class CartService {
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public List<CartDto> getCartItems(String account) {
        if (!memberRepository.existsByAccount(account)) {
            throw new UsernameNotFoundException("Account '%s' not found".formatted(account));
        }

        return cartRepository.findByMemberAccount(account)
                .stream()
                .map(CartDto::fromEntity)
                .toList();
    }

    @Transactional
    public void updateCartItem(Long cartId, CartUpdateRequest request, String account) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));

        if (!Objects.equals(account, cart.getMember().getAccount())) {
            throw new AccessDeniedException("Can not update other's cart item");
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
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));

        if (!Objects.equals(account, cart.getMember().getAccount())) {
            throw new AccessDeniedException("Can not remove other's cart item");
        }

        cartRepository.delete(cart);
    }

    @Transactional
    public void addCartItem(CartAddRequest request, String account) {
        Integer inputQuantity = request.getQuantity();
        if (inputQuantity == null || inputQuantity <= 0) {
            throw new IllegalArgumentException("Quantity must be more than 0");
        }

        Member member = memberRepository.findByAccount(account)
                .orElseThrow(() -> new UsernameNotFoundException("Account '%s' not found".formatted(account)));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        if (Objects.equals(account, product.getMember().getAccount())) {
            throw new AccessDeniedException("Can not add own product into cart");
        }

        Cart cart = cartRepository.findByMemberAndProduct(member, product)
                .orElseGet(() -> cartRepository.save(Cart.create(member, product)));

        int targetQuantity = cart.getQuantity() + inputQuantity;
        if (targetQuantity > product.getQuantity()) {
            throw new IllegalArgumentException("Product quantity is not enough");
        } else {
            cart.setQuantity(targetQuantity);
        }

        cartRepository.save(cart);
    }
}