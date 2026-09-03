package com.danny.shoppingplatform.repository;

import com.danny.shoppingplatform.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByMemberId(Long memberId);

    List<Order> findByVendorId(Long vendorId);
}
