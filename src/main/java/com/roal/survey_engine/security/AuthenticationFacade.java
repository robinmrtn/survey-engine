package com.roal.survey_engine.security;

import com.roal.survey_engine.domain.user.UserAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {

    private final UserDetailsService userDetailsService;

    public AuthenticationFacade(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext()
            .getAuthentication();
    }

    public UserDetails getUserDetails() {
        SecurityContext context = SecurityContextHolder.getContext();
        var principal = (String) context
            .getAuthentication()
            .getPrincipal();

        return userDetailsService.loadUserByUsername(principal);
    }

    public boolean isAdmin() {
        if (getUserDetails() == null) {
            return false;
        }
        return getUserDetails()
            .getAuthorities()
            .stream()
            .anyMatch(authority -> UserAuthority.ROLE_ADMIN.toString().equals(authority.getAuthority()));
    }
}
