package com.danny.shoppingplatform.dto.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record AddOrderRequest(
        @NotEmpty(message = "Cart IDs should not be empty") List<Long> cartIds,
        @NotNull(message = "Vendor ID should not be null") Long vendorId,
        String receiverName,
        String receiverPhone,
        String receiverEmail,
        String receiverAddress,
        String paymentMethod,
        String note,
        BigDecimal shippingFee
) {
}
