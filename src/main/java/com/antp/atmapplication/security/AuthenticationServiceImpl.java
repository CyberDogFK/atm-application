package com.antp.atmapplication.security;

import com.antp.atmapplication.exception.AuthenticationException;
import com.antp.atmapplication.model.Role;
import com.antp.atmapplication.model.User;
import com.antp.atmapplication.service.RoleService;
import com.antp.atmapplication.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = Logger.getLogger("Authentication logger");

    public AuthenticationServiceImpl(UserService userService,
                                     RoleService roleService,
                                     PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(String name, String password) {
        User user = new User();
        user.setName(name);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(roleService.getRoleByName(Role.RoleName.USER));
        user.setAccounts(new ArrayList<>());
        user = userService.save(user);
        return user;
    }

    @Override
    public User login(String login, String password) throws AuthenticationException {
        logger.log(Level.INFO, "Login");
        Optional<User> user = userService.findByName(login);
        String encodedPassword = passwordEncoder.encode(password);
        if (user.isEmpty() || user.get().getPassword().equals(encodedPassword)) {
            throw new AuthenticationException("Incorrect username of password");
        }
        return user.get();
    }
}
