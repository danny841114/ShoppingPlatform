package com.danny.shoppingplatform.dto.order;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddOrderRequest {
    @NotEmpty
    private List<Long> cartIds;
}
