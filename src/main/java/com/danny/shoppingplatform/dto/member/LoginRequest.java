package com.danny.shoppingplatform.dto.member;

import lombok.Data;

@Data
public class LoginRequest {
    private String account;
    private String password;
}
