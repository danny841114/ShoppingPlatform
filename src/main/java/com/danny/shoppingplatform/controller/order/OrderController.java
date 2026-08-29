package com.danny.shoppingplatform.controller.order;

import com.danny.shoppingplatform.annotation.CurrentAccount;
import com.danny.shoppingplatform.dto.order.AddOrderRequest;
import com.danny.shoppingplatform.dto.order.OrderResponse;
import com.danny.shoppingplatform.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> addOrder(@RequestBody AddOrderRequest request, @CurrentAccount String account) {
        OrderResponse orderResponse = orderService.addOrder(request, account);
        return ResponseEntity.ok(orderResponse);
    }
}
