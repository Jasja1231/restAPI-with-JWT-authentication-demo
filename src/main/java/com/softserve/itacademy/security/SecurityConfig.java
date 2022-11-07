package com.softserve.itacademy.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


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
    private static final String ADMIN = "/api/**";
    private static final String USER_FIRST = "/api/users/todos/allToDos";
    private static final String USER_SECOND = "/api/users/todos/allToDos";
    private static final String USER_THIRD = "/api/users/{userId}/todos/{todoId}/collaborators";
    private static final String USER = "/api/users/{userId}/todos/{todoId}/tasks";

    private static final String LOGIN = "/api/auth/login";
    private static final String REGISTER = "/api/users/";

    private final JwtProvider jwtProvider;

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
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(LOGIN).permitAll()
                .antMatchers(REGISTER).permitAll()
//            .antMatchers(ADMIN).hasRole("ADMIN")
//            .antMatchers(USER_FIRST).hasRole("USER")
//            .antMatchers(USER_SECOND).hasRole("USER")
//            .antMatchers(USER_THIRD).hasRole("USER")
//            .antMatchers(USER).hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtProvider));
    }
}

