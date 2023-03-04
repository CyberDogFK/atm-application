package com.antp.atmapplication.security;

import com.antp.atmapplication.lib.AuthenticationException;
import com.antp.atmapplication.model.User;

public interface AuthenticationService {
    User register(String name, String password);

    User login(String login, String password) throws AuthenticationException;
}
