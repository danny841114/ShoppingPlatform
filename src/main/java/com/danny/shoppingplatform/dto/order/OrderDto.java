package com.danny.shoppingplatform.dto.order;

import com.danny.shoppingplatform.model.Order;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private String orderNumber;
    private BigDecimal totalAmount;
    private BigDecimal shippingFee;
    private String status;
    private String receiverName;
    private String receiverPhone;
    private String receiverEmail;
    private String receiverAddress;
    private String paymentMethod;
    private String note;
    private Instant createdDate;
    private MemberDetail member;
    private VendorDetail vendor;
    private List<OrderItem> items;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberDetail {
        private Long id;
        private String account;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VendorDetail {
        private Long id;
        private String shopName;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {
        private Long productId;
        private String productName;
        private BigDecimal price;
        private Integer quantity;
    }

    public static OrderDto fromEntity(Order order) {
        List<OrderItem> items = order.getOrderItemList()
                .stream()
                .map(item -> OrderItem.builder()
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .price(item.getPrice())
                        .quantity(item.getQuantity())
                        .build()
                )
                .collect(Collectors.toList());

        MemberDetail member = MemberDetail.builder()
                .id(order.getMember().getId())
                .account(order.getMember().getUser().getAccount())
                .build();

        VendorDetail vendor = VendorDetail.builder()
                .id(order.getVendor().getId())
                .shopName(order.getVendor().getShopName())
                .build();

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
