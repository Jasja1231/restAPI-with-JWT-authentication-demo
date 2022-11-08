package com.softserve.itacademy.controller;

import com.softserve.itacademy.dto.AuthRequestUserDto;
import com.softserve.itacademy.dto.AuthResponseUserDto;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.security.JwtProvider;
import com.softserve.itacademy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class JwtAuthRestController {


    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserService userService;

    @Autowired
    public JwtAuthRestController(AuthenticationManager authenticationManager, JwtProvider jwtProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.userService = userService;
    }


    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity login(@RequestBody AuthRequestUserDto requestUserDto) {
        try {
            String username = requestUserDto.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestUserDto.getPassword()));
            Optional<User> userFromDB = userService.findByEmail(username);
            if (userFromDB.isEmpty()) {
                throw new UsernameNotFoundException("Cannot authenticate user, invalid username.");
            }
            String token = jwtProvider.createToken(username, userFromDB.get().getRole());

//            Map<Object,Object> response = new HashMap<>();
//            response.put("username",username);
//            response.put("token",token);
            AuthResponseUserDto responseData = new AuthResponseUserDto();
            responseData.setUsername(username);
            responseData.setToken(token);

            return ResponseEntity.ok(responseData);

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Cannot authenticate user, invalid data.");
        }
    }
}
