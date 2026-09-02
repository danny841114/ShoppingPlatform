package com.danny.shoppingplatform.dto.member;

import com.danny.shoppingplatform.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String account;
    private String role;
    private String name;
    private LocalDate birthdate;
    private String email;
    private byte[] photo;

    public static UserDto fromEntity(User user) {
        if (user == null) return null;

        return UserDto.builder()
                .id(user.getId())
                .account(user.getAccount())
                .role(user.getRole())
                .name(user.getMember().getName())
                .birthdate(user.getMember().getBirthdate())
                .email(user.getMember().getEmail())
                .photo(user.getMember().getPhoto())
                .build();
    }
}
