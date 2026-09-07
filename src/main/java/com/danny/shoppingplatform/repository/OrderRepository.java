package com.danny.shoppingplatform.repository;

import com.danny.shoppingplatform.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("""
                SELECT DISTINCT o FROM Order o
                JOIN FETCH o.member m
                JOIN FETCH m.user
                JOIN FETCH o.vendor v
                LEFT JOIN FETCH o.orderItemList i
                LEFT JOIN FETCH i.product
                WHERE o.member.id = :memberId
            """)
    List<Order> findByMemberIdWithDetails(@Param("memberId") Long memberId);

    @Query("""
                SELECT DISTINCT o FROM Order o
                JOIN FETCH o.member m
                JOIN FETCH m.user
                JOIN FETCH o.vendor v
                LEFT JOIN FETCH o.orderItemList i
                LEFT JOIN FETCH i.product
                WHERE o.vendor.id = :vendorId
            """)
    List<Order> findByVendorIdWithDetails(@Param("vendorId") Long vendorId);
}
