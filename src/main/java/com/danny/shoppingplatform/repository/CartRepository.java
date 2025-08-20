package com.danny.shoppingplatform.repository;

import com.danny.shoppingplatform.model.Cart;
import com.danny.shoppingplatform.model.Member;
import com.danny.shoppingplatform.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findByMember(Member member);
    Optional<Cart> findByMemberAndProduct(Member member, Product product);
}