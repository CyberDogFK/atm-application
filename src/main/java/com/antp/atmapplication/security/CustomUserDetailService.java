package com.antp.atmapplication.security;

import com.antp.atmapplication.model.User;
import com.antp.atmapplication.service.UserService;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserService userService;

    public CustomUserDetailService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userService.findByName(username);
        UserBuilder userBuilder;
        if (optionalUser.isPresent()) {
            User presentUser  = optionalUser.get();
            userBuilder = org.springframework.security.core.userdetails.User.withUsername(username);
            userBuilder.password(presentUser.getPassword());
            userBuilder.roles(presentUser.getRole().getName().name());
            return userBuilder.build();
        }
        throw new UsernameNotFoundException("User not found");
    }
}
