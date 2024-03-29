package com.antp.atmapplication.service;

import com.antp.atmapplication.model.Account;
import com.antp.atmapplication.model.User;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAll();

    User getById(Long id);

    User save(User user);

    Optional<User> findByName(String name);

    User findUserFromAuthentication(Authentication authentication);

}
