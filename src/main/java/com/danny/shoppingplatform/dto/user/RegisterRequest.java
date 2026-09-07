package com.danny.shoppingplatform.dto.user;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank(message = "Account should not be blank") String account,
        @NotBlank(message = "Password should not be blank") String password) {
}
