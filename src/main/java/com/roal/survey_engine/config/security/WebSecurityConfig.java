package com.roal.survey_engine.config.security;

import com.roal.survey_engine.domain.user.service.UserService;
import com.roal.survey_engine.security.DefaultAccessDeniedHandler;
import com.roal.survey_engine.security.RestAuthenticationFailureHandler;
import com.roal.survey_engine.security.RestAuthenticationSuccessHandler;
import com.roal.survey_engine.security.RestUsernamePasswordAuthenticationFilter;
import com.roal.survey_engine.security.jwt.JwtRequestFilter;
import com.roal.survey_engine.security.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    private final UserDetailsService userDetailsService;

    private final TokenProvider tokenProvider;
    private final RestAuthenticationSuccessHandler restAuthenticationSuccessHandler;
    private final UserService userService;

    public WebSecurityConfig(UserDetailsService userDetailsService, TokenProvider tokenProvider,
                             RestAuthenticationSuccessHandler restAuthenticationSuccessHandler, UserService userService) {
        this.userDetailsService = userDetailsService;
        this.tokenProvider = tokenProvider;
        this.restAuthenticationSuccessHandler = restAuthenticationSuccessHandler;
        this.userService = userService;
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//            .withUser("user")
//            .password("{noop}test")
//            .roles("USER");
//    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.cors();

        // @formatter:off
        http
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .accessDeniedHandler(accessDeniedHandler())
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/surveys/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/campaigns/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/responses/campaigns/**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
            .antMatchers("/v3/api-docs/**").permitAll()
            .anyRequest().authenticated();

        http.addFilterBefore(new JwtRequestFilter("/**", tokenProvider, userDetailsService),
            UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(restUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        // @formatter:on
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler() {
        return new DefaultAccessDeniedHandler();
    }

    @Bean
    AuthenticationFailureHandler authenticationFailureHandler() {
        return new RestAuthenticationFailureHandler();
    }

    @Bean
    AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new RestAuthenticationSuccessHandler(userService, tokenProvider);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public RestUsernamePasswordAuthenticationFilter restUsernamePasswordAuthenticationFilter() throws Exception {
        var filter = new RestUsernamePasswordAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setAuthenticationSuccessHandler(restAuthenticationSuccessHandler);
        filter.setRequiresAuthenticationRequestMatcher(
            new AntPathRequestMatcher("/api/authentication", HttpMethod.POST.name()));

        return filter;

    }
}
