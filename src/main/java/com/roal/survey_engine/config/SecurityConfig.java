package com.roal.survey_engine.config;

import com.roal.survey_engine.security.DefaultAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user").password("test").roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        // for h2 console
        http.headers().frameOptions().disable();

        http.cors()
            .and()
            .formLogin().permitAll()
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.FORBIDDEN))
            .accessDeniedHandler(accessDeniedHandler())

            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/api/surveys/**").permitAll()
            .antMatchers(HttpMethod.POST, "/api/responses/campaigns/**").permitAll()
            .antMatchers("/h2/**").permitAll()
            .anyRequest().authenticated();
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler() {
        return new DefaultAccessDeniedHandler();
    }
}
