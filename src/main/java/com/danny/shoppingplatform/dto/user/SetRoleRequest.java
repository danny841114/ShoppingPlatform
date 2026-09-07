package com.danny.shoppingplatform.dto.user;

import jakarta.validation.constraints.NotBlank;

public record SetRoleRequest(@NotBlank(message = "Role should not be blank") String role){}
