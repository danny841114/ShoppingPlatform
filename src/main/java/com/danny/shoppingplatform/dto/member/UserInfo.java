package com.danny.shoppingplatform.dto.member;

import com.danny.shoppingplatform.dto.user.CustomUserDetails;
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
    private Long userId;
    private Long memberId;
    private Long vendorId;
    private List<String> roles;

    public static UserInfo fromEntity(User user) {
        Long memberId = user.getMember() != null ? user.getMember().getId() : null;
        Long vendorId = user.getVendor() != null ? user.getVendor().getId() : null;

        return UserInfo.builder()
                .account(user.getAccount())
                .userId(user.getId())
                .memberId(memberId)
                .vendorId(vendorId)
                .roles(user.getRoles())
                .build();
    }

    public static UserInfo fromEntity(CustomUserDetails userDetails) {
        return UserInfo.builder()
                .account(userDetails.getUsername())
                .userId(userDetails.getUserId())
                .memberId(userDetails.getMemberId())
                .vendorId(userDetails.getVendorId())
                .roles(userDetails.getRoles())
                .build();
    }
}
