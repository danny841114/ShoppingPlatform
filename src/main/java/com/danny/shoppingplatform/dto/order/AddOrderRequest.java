package com.danny.shoppingplatform.dto.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class AddOrderRequest {
    @NotEmpty
    private List<Long> cartIds;

    @NotNull
    private Long vendorId;

    private String receiverName;
    private String receiverPhone;
    private String receiverEmail;
    private String receiverAddress;
    private String paymentMethod;
    private String note;
    private BigDecimal shippingFee;
}
