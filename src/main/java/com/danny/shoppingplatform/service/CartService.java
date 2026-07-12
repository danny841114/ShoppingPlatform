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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.danny.shoppingplatform.dto.cart.CartDto.fromEntity;

@RequiredArgsConstructor
@Service
public class CartService {
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public List<Cart> getCartListByMember(Integer memberId) {
        Member member = memberRepository.findById(memberId).orElse(null);
        return cartRepository.findByMember(member);
    }

    public void deleteProductFromCart(Integer cartId) {
        cartRepository.deleteById(cartId);
    }

    @Transactional
    public CartDto addProductIntoCart(Integer productId, CartAddRequest request, String account) {
        Member member = memberRepository.findByAccount(account)
                .orElseThrow(() -> new UsernameNotFoundException("Account '%s' not found".formatted(account)));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("商品不存在"));

        if (!Objects.equals(member.getId(), product.getMember().getId())) {
            throw new IllegalArgumentException("不能將自己的商品加入購物車");
        }

        Integer inputQuantity = request.getQuantity();
        if (inputQuantity == null || inputQuantity <= 0) {
            throw new IllegalArgumentException("加入購物車的數量必須大於 0");
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
            throw new IllegalArgumentException("商品庫存不足，無法加入更多數量");
        } else {
            cart.setQuantity(targetQuantity);
        }

        Cart savedCart = cartRepository.save(cart);

        return fromEntity(savedCart);
    }

    public Cart increaseProductQuantity(Integer cartId) {
        Optional<Cart> cartOptional = cartRepository.findById(cartId);
        if (cartOptional.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Cart cart = cartOptional.get();
        if (cart.getQuantity() + 1 <= cart.getProduct().getQuantity()) {
            cart.setQuantity(cart.getQuantity() + 1);
            cartRepository.save(cart);
        }

        return cart;
    }

    public Cart decreaseProductQuantity(Integer cartId) {
        Optional<Cart> cartOptional = cartRepository.findById(cartId);
        if (cartOptional.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Cart cart = cartOptional.get();
        if (cart.getQuantity() > 1) {
            cart.setQuantity(cart.getQuantity() - 1);
            cartRepository.save(cart);
        }

        return cart;
    }
}