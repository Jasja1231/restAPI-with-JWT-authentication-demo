package com.softserve.itacademy.security;

import com.softserve.itacademy.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserDetailsImpl implements org.springframework.security.core.userdetails.UserDetails {
    private final User user;

    public String getFirstName() {
        return firstName;
    }

    private String firstName;


    public UserDetailsImpl(User user) {
        this.user = user;
        this.firstName = user.getFirstName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
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

    public User getUser()
    {
        return this.user;
    }
}
