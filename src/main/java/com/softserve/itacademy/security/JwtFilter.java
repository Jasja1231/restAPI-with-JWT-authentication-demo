package com.softserve.itacademy.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;

    public JwtFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        String token = jwtProvider.resolveToken((HttpServletRequest) request);
//        try {
//            if(token != null && jwtProvider.validateToken(token)){
//                Authentication authentication = jwtProvider.getAuthentication(token);
//
//                if(authentication != null){
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                }
//                chain.doFilter(request,response);
//            }
//        } catch (JwtAuthenticationException e) {
//            throw new RuntimeException(e);
//        }


        String token = jwtProvider.resolveToken((HttpServletRequest) request);
        if (token != null && jwtProvider.validateToken(token)) {
            Authentication auth = jwtProvider.getAuthentication(token);

            if (auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        chain.doFilter(request, response);
    }
}
