package com.antp.atmapplication.controller;

import com.antp.atmapplication.dto.UserLoginDto;
import com.antp.atmapplication.dto.UserRegistrationDto;
import com.antp.atmapplication.dto.UserResponseDto;
import com.antp.atmapplication.exception.AuthenticationException;
import com.antp.atmapplication.model.User;
import com.antp.atmapplication.security.AuthenticationService;
import com.antp.atmapplication.security.jwt.JwtTokenProvider;
import com.antp.atmapplication.service.mapper.ResponseDtoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final ResponseDtoMapper<UserResponseDto, User> responseDtoMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final Logger logger = Logger.getLogger("Controller logger");

    public AuthenticationController(AuthenticationService authenticationService,
                                    ResponseDtoMapper<UserResponseDto, User> responseDtoMapper,
                                    JwtTokenProvider jwtTokenProvider) {
        this.authenticationService = authenticationService;
        this.responseDtoMapper = responseDtoMapper;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public UserResponseDto register(@RequestBody UserRegistrationDto userRegistrationDto) {
        logger.log(Level.INFO, "register");
        User user = authenticationService.register(userRegistrationDto.getName(),
                userRegistrationDto.getPassword());
        return responseDtoMapper.mapToDto(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserLoginDto userLoginDto)
            throws AuthenticationException {
        logger.log(Level.INFO, "login");
        User user = authenticationService.login(userLoginDto.getName(),
                userLoginDto.getPassword());
        String token = jwtTokenProvider.createToken(user.getName(), user.getRole().getName().name());
        logger.log(Level.INFO, "token:" +  token);
        return new ResponseEntity<>(Map.of("token", token), HttpStatus.OK);
    }
}
