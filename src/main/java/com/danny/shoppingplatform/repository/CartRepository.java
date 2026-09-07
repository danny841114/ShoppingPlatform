package com.danny.shoppingplatform.repository;

import com.danny.shoppingplatform.model.CartItem;
import com.danny.shoppingplatform.model.Member;
import com.danny.shoppingplatform.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByMemberUserAccount(String account);

    Optional<CartItem> findByMemberAndProduct(Member member, Product product);

    List<CartItem> findByIdInAndMemberId(List<Long> ids, Long memberId);

    void deleteByIdInAndMemberId(List<Long> ids, Long memberId);
}