package com.roal.survey_engine.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roal.survey_engine.domain.user.dto.AuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RestUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        try {
            AuthenticationRequest authenticationRequest = objectMapper
                .readValue(request.getReader(), AuthenticationRequest.class);

            var usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(authenticationRequest.username(),
                authenticationRequest.password());

            setDetails(request, usernamePasswordAuthenticationToken);

            return getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);


        } catch (IOException e) {
            throw new InternalAuthenticationServiceException("Login failed: " + e.getMessage());
        }
    }
}
