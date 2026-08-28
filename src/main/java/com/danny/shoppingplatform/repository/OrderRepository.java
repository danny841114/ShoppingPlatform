package com.danny.shoppingplatform.repository;

import com.danny.shoppingplatform.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
