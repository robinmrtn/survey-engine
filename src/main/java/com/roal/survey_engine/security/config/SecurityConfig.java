package com.roal.survey_engine.security.config;

import com.roal.survey_engine.security.DefaultAccessDeniedHandler;
import com.roal.survey_engine.security.RESTAuthenticationFailureHandler;
import com.roal.survey_engine.security.RESTAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("user")
            .password("{noop}test")
            .roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.cors();

        // for h2 console
        http.headers().frameOptions().disable();

        // @formatter:off
        http
            .formLogin()
            .loginProcessingUrl("/api/authentication")
            .failureHandler(authenticationFailureHandler())
            .successHandler(authenticationSuccessHandler())
            .permitAll()

            .and()
            .exceptionHandling()
            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.FORBIDDEN))
            .accessDeniedHandler(accessDeniedHandler())

            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/api/surveys/**").permitAll()
            .antMatchers(HttpMethod.POST, "/api/responses/campaigns/**").permitAll()
            .antMatchers("/h2/**").permitAll()
            .antMatchers("/swagger-ui/**").permitAll()
            .antMatchers("/v3/api-docs/**").permitAll()
            .anyRequest().authenticated();
        // @formatter:on
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler() {
        return new DefaultAccessDeniedHandler();
    }

    @Bean
    AuthenticationFailureHandler authenticationFailureHandler() {
        return new RESTAuthenticationFailureHandler();
    }

    @Bean
    AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new RESTAuthenticationSuccessHandler();
    }
}
