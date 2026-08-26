package com.danny.shoppingplatform.service;

import com.danny.shoppingplatform.dto.cart.CartAddRequest;
import com.danny.shoppingplatform.dto.cart.CartDto;
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
    public void removeCartItem(Long cartId, String account) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));

        if (!Objects.equals(account, cart.getMember().getAccount())) {
            throw new AccessDeniedException("Can not remove other's cart item");
        }

        cartRepository.delete(cart);
    }

    @Transactional
    public CartDto addCartItem(Long productId, CartAddRequest request, String account) {
        Member member = memberRepository.findByAccount(account)
                .orElseThrow(() -> new UsernameNotFoundException("Account '%s' not found".formatted(account)));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        if (!Objects.equals(account, product.getMember().getAccount())) {
            throw new AccessDeniedException("Can not add own product into cart");
        }

        Integer inputQuantity = request.getQuantity();
        if (inputQuantity == null || inputQuantity <= 0) {
            throw new IllegalArgumentException("Quantity must be more than 0");
        }

        Cart cart = cartRepository.findByMemberAndProduct(member, product)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setCreatedDate(Instant.now());
                    newCart.setQuantity(0);
                    newCart.setMember(member);
                    newCart.setProduct(product);
                    return newCart;
                });

        int targetQuantity = cart.getQuantity() + inputQuantity;
        if (targetQuantity > product.getQuantity()) {
            throw new IllegalArgumentException("Product quantity is not enough");
        } else {
            cart.setQuantity(targetQuantity);
        }

        Cart savedCart = cartRepository.save(cart);

        return fromEntity(savedCart);
    }

    @Transactional
    public CartDto increaseProductQuantity(Long cartId, String account) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));

        if (!Objects.equals(account, cart.getMember().getAccount())) {
            throw new AccessDeniedException("Can not handle other's cart item");
        }

        if (cart.getQuantity() + 1 <= cart.getProduct().getQuantity()) {
            cart.setQuantity(cart.getQuantity() + 1);
            cartRepository.save(cart);
        }

        return fromEntity(cart);
    }

    @Transactional
    public CartDto decreaseProductQuantity(Long cartId, String account) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));

        if (!Objects.equals(account, cart.getMember().getAccount())) {
            throw new AccessDeniedException("Can not handle other's cart item");
        }

        if (cart.getQuantity() > 1) {
            cart.setQuantity(cart.getQuantity() - 1);
            cartRepository.save(cart);
        }

        return fromEntity(cart);
    }
}