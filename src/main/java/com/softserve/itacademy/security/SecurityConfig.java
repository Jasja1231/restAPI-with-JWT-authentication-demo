package com.softserve.itacademy.security;

import com.softserve.itacademy.service.impl.JwtUserDetailServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Slf4j
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //    @Autowired
//    UserDetailsService userServiceImpl;
//    @Autowired
//    private JwtFilter jwtFilter;
//
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userServiceImpl);
//    }
//
//
//    @Bean
//    PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .exceptionHandling(e -> e.authenticationEntryPoint(restAuthenticationEntryPoint())
//                )
//                .httpBasic(h -> h.authenticationEntryPoint(restAuthenticationEntryPoint()))
//                .csrf().disable()
//                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeRequests()
//                .antMatchers("/signin").permitAll()
//                .antMatchers("/api/users/*").hasAnyRole("USER", "ADMIN")
//                .mvcMatchers(HttpMethod.DELETE, "/api/users/*").hasRole("ADMIN")
//                .anyRequest().permitAll()
//                .and()
//                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//
//
//    }
//
//    @Bean
//    AuthenticationEntryPoint restAuthenticationEntryPoint() {
//        return (request, response, authException) -> {
//            log.warn("Authentication for '{} {}' failed with error: {}",
//                    request.getMethod(), request.getRequestURL(), authException.getMessage());
//            response.sendError(UNAUTHORIZED.value(), authException.getMessage());
//        };
//    }
//
//}

    private static final String LOGIN = "/api/auth/login";
    private static final String REGISTER = "/api/users/";

    private final JwtProvider jwtProvider;

   @Autowired
   JwtUserDetailServiceImpl userServiceImpl;

    @Autowired
    public SecurityConfig(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userServiceImpl);
    }

    @Autowired //TODO: this bean already registered in jwtProvider but maybe it should be here instead
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/api/auth/login").permitAll()
                .antMatchers(HttpMethod.POST,"/api/users").permitAll()
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtProvider));
    }
}

