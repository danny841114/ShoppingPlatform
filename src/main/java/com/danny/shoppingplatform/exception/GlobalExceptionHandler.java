package com.danny.shoppingplatform.exception;

import com.danny.shoppingplatform.dto.error.CustomErrorResponse;
import com.danny.shoppingplatform.exception.custom.CustomAccountNotFoundException;
import com.danny.shoppingplatform.exception.custom.InternalServerException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.auth.login.AccountNotFoundException;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<CustomErrorResponse> handleSystemError(InternalServerException ex) {
        log.error("Internal server error: ", ex);
        return ResponseEntity.internalServerError()
                .body(CustomErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "SYSTEM_ERROR", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleUncaughtError(Exception ex) {
        log.error("Unknown Server Error", ex);
        return ResponseEntity.internalServerError()
                .body(CustomErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "UNKNOWN_SERVER_ERROR", "Unknown server error"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
                .body(CustomErrorResponse.of(HttpStatus.BAD_REQUEST, "INVALID_PARAMETER", ex.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<CustomErrorResponse> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.badRequest()
                .body(CustomErrorResponse.of(HttpStatus.BAD_REQUEST, "BAD_REQUEST", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleNotValidArgument(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("請求參數格式不符");

        return ResponseEntity.badRequest()
                .body(CustomErrorResponse.of(HttpStatus.BAD_REQUEST, "ARGUMENT_NOT_VALID", errorMessage));
    }

    @ExceptionHandler({EntityNotFoundException.class, AccountNotFoundException.class, CustomAccountNotFoundException.class})
    public ResponseEntity<CustomErrorResponse> handleNotFound(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(CustomErrorResponse.of(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CustomErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(CustomErrorResponse.of(HttpStatus.UNAUTHORIZED, "BAD_CREDENTIAL", ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CustomErrorResponse> handleAuthorization(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(CustomErrorResponse.of(HttpStatus.FORBIDDEN, "FORBIDDEN", ex.getMessage()));
    }
}
