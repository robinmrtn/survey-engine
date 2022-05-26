package com.roal.survey_engine.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roal.survey_engine.domain.user.dto.TokenResponse;
import com.roal.survey_engine.domain.user.service.UserService;
import com.roal.survey_engine.security.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    public RestAuthenticationSuccessHandler(@Lazy UserService userService, TokenProvider tokenProvider) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        setRedirectStrategy(new NoRedirectStrategy());
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        userService.updateLastLoginByUsername(username, request.getRemoteAddr());

        super.onAuthenticationSuccess(request, response, authentication);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(new TokenResponse(tokenProvider.generateToken(username))));
        response.getWriter().close();
        response.setStatus(HttpStatus.NO_CONTENT.value());
    }
}
