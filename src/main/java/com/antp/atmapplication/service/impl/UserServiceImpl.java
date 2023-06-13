package com.antp.atmapplication.service.impl;

import com.antp.atmapplication.exception.DataProcessingException;
import com.antp.atmapplication.model.User;
import com.antp.atmapplication.repository.UserRepository;
import com.antp.atmapplication.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new DataProcessingException("Can't find user by Id: " + id));
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByName(String name) {
        return userRepository.findUserByName(name);
    }

    @Override
    public User findUserFromAuthentication(Authentication authentication) {
        return findByName(authentication.getName()).orElseThrow(() ->
                new RuntimeException("Can't find your account with name "
                        + authentication.getName()));
    }
}
