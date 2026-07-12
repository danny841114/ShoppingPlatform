package com.danny.shoppingplatform.dto.member;

import com.danny.shoppingplatform.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private final Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getAccount();
    }

    public Member getMember() {return member;}

    public String getAccount() {
        return member.getAccount();
    }

    public String getRole() {
        return member.getRole();
    }

    public int getId() {
        return member.getId();
    }
}