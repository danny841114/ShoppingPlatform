package com.danny.shoppingplatform.dto.user;

import lombok.Data;

@Data
public class LoginRequest {
    private String account;
    private String password;
}
