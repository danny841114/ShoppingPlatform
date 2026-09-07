package com.danny.shoppingplatform.dto.error;


import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
public record CustomErrorResponse(
        Integer status,
        String code,
        String message,
        LocalDateTime timestamp
) {
    public static CustomErrorResponse of(HttpStatus statusCode, String code, String message) {
        return CustomErrorResponse.builder()
                .status(statusCode.value())
                .code(code)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
