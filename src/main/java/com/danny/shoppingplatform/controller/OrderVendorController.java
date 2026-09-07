package com.danny.shoppingplatform.controller;

import com.danny.shoppingplatform.annotation.CurrentAccount;
import com.danny.shoppingplatform.dto.order.OrderDto;
import com.danny.shoppingplatform.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/vendor/orders")
public class OrderVendorController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDto>> getOrders(@CurrentAccount String account) {
        List<OrderDto> orders = orderService.getOrdersByVendor(account);
        return ResponseEntity.ok(orders);
    }
}
