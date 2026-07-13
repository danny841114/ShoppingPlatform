package com.danny.shoppingplatform.exception;

import com.danny.shoppingplatform.dto.error.CustomErrorResponse;
import com.danny.shoppingplatform.exception.custom.CustomAccountNotFoundException;
import com.danny.shoppingplatform.exception.custom.InternalServerException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.auth.login.AccountNotFoundException;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<CustomErrorResponse> handleInternalServerException(InternalServerException ex) {
        log.error("Interval server error: ", ex);

        CustomErrorResponse error = CustomErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .code("SYSTEM_ERROR")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.internalServerError().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleUncaughtException(Exception ex) {
        log.error("Unknown Server Error", ex);

        CustomErrorResponse error = CustomErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .code("UNKNOWN_SERVER_ERROR")
                .message("Unknown server error")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.internalServerError().body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        CustomErrorResponse customErrorResponse = CustomErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .code("INVALID_PARAMETER")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.badRequest().body(customErrorResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleNotFound(EntityNotFoundException ex) {
        CustomErrorResponse customErrorResponse = CustomErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .code("RESOURCE_NOT_FOUND")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.badRequest().body(customErrorResponse);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<CustomErrorResponse> handleIllegalArgument(BadRequestException ex) {
        CustomErrorResponse customErrorResponse = CustomErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .code("BAD_REQUEST")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.badRequest().body(customErrorResponse);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleAccountNotFound(AccountNotFoundException ex) {
        CustomErrorResponse error = CustomErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .code("ACCOUNT_NOT_FOUND")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(CustomAccountNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleAccountNotFound(CustomAccountNotFoundException ex) {
        CustomErrorResponse error = CustomErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .code("ACCOUNT_NOT_FOUND")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CustomErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        CustomErrorResponse error = CustomErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .code("BAD_CREDENTIAL")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
}
