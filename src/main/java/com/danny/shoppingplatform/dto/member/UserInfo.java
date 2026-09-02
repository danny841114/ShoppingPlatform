package com.danny.shoppingplatform.dto.member;

import com.danny.shoppingplatform.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private String account;
    private List<String> roles;
    private Long userId;
    private Long memberId;
    private Long vendorId;

    public static UserInfo fromEntity(User user) {
        Long memberId = user.getMember() != null ? user.getMember().getId() : null;
        Long vendorId = user.getVendor() != null ? user.getVendor().getId() : null;

        return UserInfo.builder()
                .account(user.getAccount())
                .roles(user.getRoles())
                .userId(user.getId())
                .memberId(memberId)
                .vendorId(vendorId)
                .build();
    }
}
