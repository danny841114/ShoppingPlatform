package com.danny.shoppingplatform.dto.error;


import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CustomErrorResponse(
        Integer status,
        String code,
        String message,
        LocalDateTime timestamp
) {
    public static CustomErrorResponse of(Integer status, String code, String message) {
        return CustomErrorResponse.builder()
                .status(status)
                .code(code)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
