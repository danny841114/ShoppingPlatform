package com.danny.shoppingplatform.service;

import com.danny.shoppingplatform.model.Cart;
import com.danny.shoppingplatform.model.Member;
import com.danny.shoppingplatform.model.Product;
import com.danny.shoppingplatform.repository.CartRepository;
import com.danny.shoppingplatform.repository.MemberRepository;
import com.danny.shoppingplatform.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.Optional;

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

    public Cart addProductIntoCart(Integer memberId,
                                   Integer productId,
                                   Integer quantity) throws AccountNotFoundException {
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        if (memberOptional.isEmpty()) {
            throw new AccountNotFoundException("會員不存在");
        }

        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            throw new EntityNotFoundException("商品不存在");
        }

        Product product = productOptional.get();
        if (memberId.equals(product.getMember().getId())) {
            throw new IllegalArgumentException("不能將自己的商品加入購物車");
        }

        Member member = memberOptional.get();
        Optional<Cart> cartOptional = cartRepository.findByMemberAndProduct(member, product);
        Cart cart;
        if (cartOptional.isPresent()) {
            cart = cartOptional.get();
            Integer originalQuantity = cart.getQuantity();

            if (originalQuantity + quantity > product.getQuantity()) {
                cart.setQuantity(product.getQuantity());
            } else {
                cart.setQuantity(originalQuantity + quantity);
            }
        } else {
            cart = new Cart();
            cart.setMember(member);
            cart.setProduct(product);
            cart.setQuantity(quantity);
        }

        cartRepository.save(cart);
        return cart;
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