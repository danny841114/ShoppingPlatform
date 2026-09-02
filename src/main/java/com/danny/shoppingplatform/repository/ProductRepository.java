package com.danny.shoppingplatform.repository;

import com.danny.shoppingplatform.model.Member;
import com.danny.shoppingplatform.model.Product;
import com.danny.shoppingplatform.model.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByVendor(Vendor vendor);

    @Query("SELECT p.photo FROM Product p WHERE p.id = :id")
    Optional<byte[]> findPhotoById(@Param("id") Integer id);

    @EntityGraph(attributePaths = {"vendor"})
    Page<Product> findByNameContainingOrVendorShopNameContaining(String nameKeyword, String accountKeyword, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"vendor"})
    Page<Product> findAll(Pageable pageable);
}
