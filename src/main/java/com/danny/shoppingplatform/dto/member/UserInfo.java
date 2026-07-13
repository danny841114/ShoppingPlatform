package com.danny.shoppingplatform.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private String account;
    private String role;

    public static UserInfo generateUserInfo(String account, String role) {
        return UserInfo.builder()
                .account(account)
                .role(role)
                .build();
    }
}
