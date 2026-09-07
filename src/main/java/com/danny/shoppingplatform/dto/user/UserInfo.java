package com.danny.shoppingplatform.dto.user;

import com.danny.shoppingplatform.model.User;
import lombok.Builder;

import java.util.List;

@Builder
public record UserInfo(
        String account,
        Long userId,
        Long memberId,
        Long vendorId,
        List<String> roles
) {
    public static UserInfo fromEntity(User user) {
        if (user == null) return null;

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
        if (userDetails == null) return null;

        return UserInfo.builder()
                .account(userDetails.getUsername())
                .userId(userDetails.getUserId())
                .memberId(userDetails.getMemberId())
                .vendorId(userDetails.getVendorId())
                .roles(userDetails.getRoles())
                .build();
    }
}
