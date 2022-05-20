package com.roal.survey_engine.domain.user.controller;

import com.roal.survey_engine.domain.user.dto.AuthenticationRequest;
import com.roal.survey_engine.domain.user.dto.TokenResponse;
import com.roal.survey_engine.security.jwt.TokenProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthenticationController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;


    public AuthenticationController(TokenProvider tokenProvider,
                                    AuthenticationManager authenticationManager) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/api/authentication")
    public ResponseEntity<TokenResponse> authentication(
            @RequestBody @Valid AuthenticationRequest authenticationRequest) {
        String password = authenticationRequest.password();
        String username = authenticationRequest.username();

        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = tokenProvider.generateToken(username);
        var jwt = new TokenResponse(token);
        var httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + token);

        return new ResponseEntity<>(jwt, httpHeaders, HttpStatus.OK);

    }
}
