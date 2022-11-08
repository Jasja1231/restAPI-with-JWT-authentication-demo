package com.softserve.itacademy.service.detail;

import com.softserve.itacademy.model.User;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.security.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtUserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public JwtUserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username/*email*/) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty()) throw new UsernameNotFoundException("User not found!");

        //Read user
        User readUser = user.get();
        JwtUser jwtUser = new JwtUser(readUser.getId(),
                readUser.getFirstName(),
                readUser.getLastName(),
                readUser.getEmail(),
                readUser.getRole(),
                readUser.getPassword());

        return jwtUser;
    }
}
