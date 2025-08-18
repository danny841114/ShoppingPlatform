package com.danny.shoppingplatform.service;

import com.danny.shoppingplatform.model.Cart;
import com.danny.shoppingplatform.model.Member;
import com.danny.shoppingplatform.model.Product;
import com.danny.shoppingplatform.repository.CartRepository;
import com.danny.shoppingplatform.repository.MemberRepository;
import com.danny.shoppingplatform.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
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

    public Cart addProductIntoCart(Integer memberId, Integer productId,Integer quantity) {
        Member member = memberRepository.findById(memberId).orElse(null);
        Product product = productRepository.findById(productId).orElse(null);

        if (member == null) {
            throw new EntityNotFoundException("會員不存在");
        }
        if (product == null) {
            throw new EntityNotFoundException("商品不存在");
        }

        if (memberId.equals(product.getMember().getId())) {
            throw new IllegalArgumentException("不能將自己的商品加入購物車");
        }

        Cart cart = new Cart();
        cart.setMember(member);
        cart.setProduct(product);
        cart.setQuantity(quantity);
        cartRepository.save(cart);
        System.err.println("進來囉");
        return cart;
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
