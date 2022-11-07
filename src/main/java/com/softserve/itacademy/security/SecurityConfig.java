package com.softserve.itacademy.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;


@Slf4j
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsService userServiceImpl;
    @Autowired
    private JwtFilter jwtFilter;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userServiceImpl);
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling(e -> e.authenticationEntryPoint(restAuthenticationEntryPoint())
                )
                .httpBasic(h -> h.authenticationEntryPoint(restAuthenticationEntryPoint()))
                .csrf().disable()
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeRequests()
                .antMatchers("/signin").permitAll()
                .antMatchers("/api/users/*").hasAnyRole("USER", "ADMIN")
                .mvcMatchers(HttpMethod.DELETE, "/api/users/*").hasRole("ADMIN")
                .anyRequest().permitAll()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


    }

    @Bean
    AuthenticationEntryPoint restAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            log.warn("Authentication for '{} {}' failed with error: {}",
                    request.getMethod(), request.getRequestURL(), authException.getMessage());
            response.sendError(UNAUTHORIZED.value(), authException.getMessage());
        };
    }

}
