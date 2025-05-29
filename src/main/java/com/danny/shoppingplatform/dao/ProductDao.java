package com.danny.shoppingplatform.dao;

import com.danny.shoppingplatform.model.Member;
import com.danny.shoppingplatform.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductDao extends JpaRepository<Product, Integer> {
    Page<Product> findByNameContaining(String name, Pageable pageable);
    List<Product> findByMember(Member member);
}
