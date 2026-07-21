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
        log.error("Interval server error: ", ex);

        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .code("SYSTEM_ERROR")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.internalServerError().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleUncaughtError(Exception ex) {
        log.error("Unknown Server Error", ex);

        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .code("UNKNOWN_SERVER_ERROR")
                .message("Unknown server error")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.internalServerError().body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .code("INVALID_PARAMETER")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .code("RESOURCE_NOT_FOUND")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<CustomErrorResponse> handleBadRequest(BadRequestException ex) {
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .code("BAD_REQUEST")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleNotValidArgument(MethodArgumentNotValidException ex) {
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .code("ARGUMENT_NOT_VALID")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleAccountNotFound(AccountNotFoundException ex) {
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .code("ACCOUNT_NOT_FOUND")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(CustomAccountNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleAccountNotFound(CustomAccountNotFoundException ex) {
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .code("ACCOUNT_NOT_FOUND")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CustomErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .code("BAD_CREDENTIAL")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CustomErrorResponse> handleAuthorization(AccessDeniedException ex) {
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .code("FORBIDDEN")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
}
