package com.antp.atmapplication.security;

import com.antp.atmapplication.lib.AuthenticationException;
import com.antp.atmapplication.model.Role;
import com.antp.atmapplication.model.User;
import com.antp.atmapplication.service.RoleService;
import com.antp.atmapplication.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

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
        user.setPassword(password);
        user.setRole(roleService.getRoleByName(Role.RoleName.USER));
        user = userService.save(user);
        return user;
    }

    @Override
    public User login(String login, String password) throws AuthenticationException {
        Optional<User> user = userService.findByName(login);
        String encodedPassword = passwordEncoder.encode(password);
        if (user.isEmpty() || user.get().getPassword().equals(encodedPassword)) {
            throw new AuthenticationException("Incorrect username of password");
        }
        return user.get();
    }
}
