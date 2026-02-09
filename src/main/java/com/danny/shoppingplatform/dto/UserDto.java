package com.danny.shoppingplatform.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String username;
    private String password;
    private String email;
    private Name name;
    private String phone;

    @Getter
    @Setter
    public static class Name{
        private String firstname;
        private String lastname;
    }
}