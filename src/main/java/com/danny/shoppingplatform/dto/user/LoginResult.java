package com.danny.shoppingplatform.dto.user;

import lombok.Builder;

@Builder
public record LoginResult(
        UserInfo userInfo,
        String token
) {
    public static LoginResult of(UserInfo userInfo, String token) {
        return LoginResult.builder()
                .userInfo(userInfo)
                .token(token)
                .build();
    }
}
