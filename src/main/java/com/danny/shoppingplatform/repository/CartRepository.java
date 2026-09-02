package com.danny.shoppingplatform.repository;

import com.danny.shoppingplatform.model.Cart;
import com.danny.shoppingplatform.model.Member;
import com.danny.shoppingplatform.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByMemberUserAccount(String account);

    Optional<Cart> findByMemberAndProduct(Member member, Product product);

    List<Cart> findByIdInAndMemberId(List<Long> ids, Long memberId);

    void deleteByIdInAndMemberId(List<Long> ids, Long memberId);
}