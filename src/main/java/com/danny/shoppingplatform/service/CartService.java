package com.danny.shoppingplatform.service;

import com.danny.shoppingplatform.model.Cart;
import com.danny.shoppingplatform.model.Member;
import com.danny.shoppingplatform.model.Product;
import com.danny.shoppingplatform.repository.CartRepository;
import com.danny.shoppingplatform.repository.MemberRepository;
import com.danny.shoppingplatform.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository,
                       MemberRepository memberRepository,
                       ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    public List<Cart> getCartListByMember(Integer memberId) {
        Member member = memberRepository.findById(memberId).orElse(null);
        return cartRepository.findByMember(member);
    }

    public void deleteProductFromCart(Integer cartId) {
        cartRepository.deleteById(cartId);
    }

    public Cart addProductIntoCart(Integer memberId, Integer productId) {
        Cart cart = new Cart();
        Member member = memberRepository.findById(memberId).orElse(null);
        Product product = productRepository.findById(productId).orElse(null);

        if (member == null || product == null) {
            throw new RuntimeException();
        }

        cart.setMember(member);
        cart.setProduct(product);
        return cartRepository.save(cart);
    }

    public Cart increaseProductQuantity(Integer cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new RuntimeException();
        }
        cart.setQuantity(cart.getQuantity() + 1);
        cartRepository.save(cart);
        return cart;
    }

    public Cart decreaseProductQuantity(Integer cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new RuntimeException();
        }
        if (cart.getQuantity() == 1) {
            return cart;
        }
        cart.setQuantity(cart.getQuantity() - 1);
        cartRepository.save(cart);
        return cart;
    }
}
