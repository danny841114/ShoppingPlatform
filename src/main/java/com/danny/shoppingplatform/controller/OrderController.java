package com.danny.shoppingplatform.controller;

import com.danny.shoppingplatform.annotation.CurrentAccount;
import com.danny.shoppingplatform.dto.order.AddOrderRequest;
import com.danny.shoppingplatform.dto.order.OrderDto;
import com.danny.shoppingplatform.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> addOrder(@RequestBody AddOrderRequest request, @CurrentAccount String account) {
        OrderDto orderDto = orderService.addOrder(request, account);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderDto);
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getOrders(@RequestParam(required = false) Long memberId,
                                                    @RequestParam(required = false) Long vendorId,
                                                    @CurrentAccount String account) {
        List<OrderDto> orders = orderService.getOrders(memberId, vendorId, account);
        return ResponseEntity.ok(orders);
    }
}
