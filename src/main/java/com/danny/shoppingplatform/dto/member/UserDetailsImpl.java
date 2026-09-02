package com.danny.shoppingplatform.dto.member;

import com.danny.shoppingplatform.model.Member;
import com.danny.shoppingplatform.model.User;
import com.danny.shoppingplatform.model.Vendor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getAccount();
    }

    public User getUser() {return user;}

    public Member getMember() {return user.getMember();}

    public Vendor getVendor() {return user.getVendor();}

    public String getAccount() {
        return user.getAccount();
    }

    public String getRole() {
        return user.getRole();
    }

    public Long getId() {
        return user.getId();
    }
}