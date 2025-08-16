package com.danny.shoppingplatform.repository;

import com.danny.shoppingplatform.model.Cart;
import com.danny.shoppingplatform.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {
    List<Cart> findByMember(Member member);
}