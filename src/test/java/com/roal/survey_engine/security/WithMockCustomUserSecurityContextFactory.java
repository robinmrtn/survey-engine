package com.roal.survey_engine.security;

import com.roal.survey_engine.domain.user.UserAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Set;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {

        Set<SimpleGrantedAuthority> grantedAuthorities =
                Set.of(new SimpleGrantedAuthority(UserAuthority.Constants.ADMIN_VALUE));
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(annotation.username(), "password", grantedAuthorities);

        SecurityContext context = SecurityContextHolder.createEmptyContext();

        context.setAuthentication(auth);

        return context;
    }
}
