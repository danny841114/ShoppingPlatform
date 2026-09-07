package com.danny.shoppingplatform.dto.order;

import com.danny.shoppingplatform.model.Order;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Builder
public record OrderDto(
        Long id,
        String orderNumber,
        BigDecimal totalAmount,
        BigDecimal shippingFee,
        String status,
        String receiverName,
        String receiverPhone,
        String receiverEmail,
        String receiverAddress,
        String paymentMethod,
        String note,
        Instant createdDate,
        MemberDetail member,
        VendorDetail vendor,
        List<OrderItem> items
) {
    @Builder
    public record MemberDetail(
            Long id,
            String account
    ) {
    }

    @Builder
    public record VendorDetail(
            Long id,
            String shopName
    ) {
    }

    @Builder
    public record OrderItem(
            Long productId,
            String productName,
            BigDecimal price,
            Integer quantity
    ) {
    }

    public static OrderDto fromEntity(Order order) {
        if (order == null) return null;

        List<OrderItem> items = (order.getOrderItemList() == null)
                ? Collections.emptyList()
                : order.getOrderItemList().stream()
                .map(item -> OrderItem.builder()
                        .productId(item.getProduct() != null ? item.getProduct().getId() : null)
                        .productName(item.getProduct() != null ? item.getProduct().getName() : null)
                        .price(item.getPrice())
                        .quantity(item.getQuantity())
                        .build())
                .toList();

        MemberDetail member = null;
        if (order.getMember() != null) {
            String account = order.getMember().getUser() != null
                    ? order.getMember().getUser().getAccount()
                    : null;
            member = MemberDetail.builder()
                    .id(order.getMember().getId())
                    .account(account)
                    .build();
        }

        VendorDetail vendor = null;
        if (order.getVendor() != null) {
            vendor = VendorDetail.builder()
                    .id(order.getVendor().getId())
                    .shopName(order.getVendor().getShopName())
                    .build();
        }

        return OrderDto.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .totalAmount(order.getTotalAmount())
                .shippingFee(order.getShippingFee())
                .status(order.getStatus())
                .receiverName(order.getReceiverName())
                .receiverPhone(order.getReceiverPhone())
                .receiverEmail(order.getReceiverEmail())
                .receiverAddress(order.getReceiverAddress())
                .paymentMethod(order.getPaymentMethod())
                .note(order.getNote())
                .createdDate(order.getCreatedDate())
                .member(member)
                .vendor(vendor)
                .items(items)
                .build();
    }
}
