package com.danny.shoppingplatform.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetRoleRequest {
    @NotBlank(message = "Role should not be blank")
    private String role;
}
