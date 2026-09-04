package com.danny.shoppingplatform.dto.user;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CustomUserDetails implements UserDetails {
    private final String username;
    private final String password;
    private final Long userId;
    private final Long memberId;
    private final Long vendorId;
    private final List<String> roles;

    public CustomUserDetails(String username,
                             String password,
                             Long userId,
                             Long memberId,
                             Long vendorId,
                             List<String> roles) {
        this.username = username;
        this.password = password;
        this.userId = userId;
        this.memberId = memberId;
        this.vendorId = vendorId;
        this.roles = roles;
    }

    public CustomUserDetails(String username,
                             Long userId,
                             Long memberId,
                             Long vendorId,
                             List<String> roles) {
        this.username = username;
        this.password = "";
        this.userId = userId;
        this.memberId = memberId;
        this.vendorId = vendorId;
        this.roles = roles;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles == null || roles.isEmpty()) return List.of();

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }
}
