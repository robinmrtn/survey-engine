package com.roal.survey_engine.security;

import com.roal.survey_engine.domain.user.UserAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext()
            .getAuthentication();
    }

    public UserDetails getUserDetails() {
        return (UserDetails) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();
    }

    public boolean isAdmin() {
        if (getAuthentication() == null) {
            return false;
        }
        return getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(authority -> UserAuthority.ROLE_ADMIN.toString().equals(authority.getAuthority()));
    }
}
