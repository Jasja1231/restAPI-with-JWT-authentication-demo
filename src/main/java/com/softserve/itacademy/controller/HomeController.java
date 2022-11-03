package com.softserve.itacademy.controller;

import com.softserve.itacademy.security.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "home"})
    public String home(SecurityContextHolderAwareRequestWrapper request) {
        long userId = ((UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal())
            .getUser()
            .getId();

        return request.isUserInRole("ADMIN")
            ? "redirect:/users/all"
            : "redirect:/todos/all/users/" + userId;
    }
}
