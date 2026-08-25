package com.danny.shoppingplatform.repository;

import com.danny.shoppingplatform.model.Member;
import com.danny.shoppingplatform.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByMember(Member member);

    @Query("SELECT p.photo FROM Product p WHERE p.id = :id")
    Optional<byte[]> findPhotoById(@Param("id") Integer id);

    @Query("SELECT DISTINCT p FROM Product p " +
            "LEFT JOIN p.member m " +
            "WHERE p.name LIKE %:keyword% " +
            "OR m.account LIKE %:keyword%")
    Page<Product> findByNameOrMemberAccountContaining(@Param("keyword") String keyword, Pageable pageable);
}
