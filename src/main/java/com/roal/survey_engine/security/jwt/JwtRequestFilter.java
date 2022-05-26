package com.roal.survey_engine.security.jwt;

import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtRequestFilter extends GenericFilterBean {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final TokenProvider tokenProvider;
    private final RequestMatcher requestMatcher;

    private final UserDetailsService userDetailsService;

    public JwtRequestFilter(String path, TokenProvider tokenProvider,
                            UserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.requestMatcher = new AntPathRequestMatcher(path);
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        final String requestTokenHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (!requiresAuthentication(request) || requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = resolveToken(requestTokenHeader);
        String subject = null;
        try {
            subject = tokenProvider.getSubjectFromToken(jwtToken);
            Authentication auth = buildAuthFromJwt(subject);
            SecurityContextHolder.getContext().setAuthentication(auth);

            filterChain.doFilter(request, response);
        } catch (JwtException exception) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        SecurityContextHolder.clearContext();
    }

    private String resolveToken(String requestTokenHeader) {
        return requestTokenHeader.substring(7);
    }

    private boolean requiresAuthentication(HttpServletRequest request) {
        return requestMatcher.matches(request);
    }

    private Authentication buildAuthFromJwt(String subject) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(subject, null, userDetails.getAuthorities());

        authenticationToken.setDetails(userDetails);

        return authenticationToken;
    }
}
