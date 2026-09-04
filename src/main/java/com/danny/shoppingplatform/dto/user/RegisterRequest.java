package com.danny.shoppingplatform.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Account should not be blank")
    private String account;

    @NotBlank(message = "Password should not be blank")
    private String password;
}
