package com.example.chat_app.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private final int userId;
    private final int loginId;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(int userId, int loginId, String password, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.loginId = loginId;
        this.password = password;
        this.authorities = authorities;
    }

    public int getUserId() {
        return userId;
    }

    public int getLoginId() {
        return loginId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return String.valueOf(loginId);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}