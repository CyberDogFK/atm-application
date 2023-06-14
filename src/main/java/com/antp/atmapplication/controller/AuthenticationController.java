package com.antp.atmapplication.controller;

import com.antp.atmapplication.dto.UserLoginDto;
import com.antp.atmapplication.dto.UserRegistrationDto;
import com.antp.atmapplication.dto.UserResponseDto;
import com.antp.atmapplication.exception.AuthenticationException;
import com.antp.atmapplication.model.User;
import com.antp.atmapplication.security.AuthenticationService;
import com.antp.atmapplication.security.jwt.JwtTokenProvider;
import com.antp.atmapplication.service.mapper.ResponseDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Tag(name = "Authentication controller")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final ResponseDtoMapper<UserResponseDto, User> responseDtoMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    public AuthenticationController(AuthenticationService authenticationService,
                                    ResponseDtoMapper<UserResponseDto, User> responseDtoMapper,
                                    JwtTokenProvider jwtTokenProvider) {
        this.authenticationService = authenticationService;
        this.responseDtoMapper = responseDtoMapper;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    @Operation(summary = "Register new user in system")
    public UserResponseDto register(@RequestBody UserRegistrationDto userRegistrationDto) {
        logger.info("register");
        User user = authenticationService.register(userRegistrationDto.getName(),
                userRegistrationDto.getPassword());
        return responseDtoMapper.mapToDto(user);
    }

    @PostMapping("/login")
    @Operation(summary = "Login new user in system and return to him jwt token")
    public ResponseEntity<Object> login(@RequestBody UserLoginDto userLoginDto)
            throws AuthenticationException {
        logger.info("login");
        User user = authenticationService.login(userLoginDto.getName(),
                userLoginDto.getPassword());
        String token = jwtTokenProvider.createToken(user.getName(), user.getRole().getName().name());
        logger.info("token:" +  token);
        return new ResponseEntity<>(Map.of("token", token), HttpStatus.OK);
    }
}
