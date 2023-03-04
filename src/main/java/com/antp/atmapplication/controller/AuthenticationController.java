package com.antp.atmapplication.controller;

import com.antp.atmapplication.dto.UserLoginDto;
import com.antp.atmapplication.dto.UserRegistrationDto;
import com.antp.atmapplication.dto.UserRequestDto;
import com.antp.atmapplication.dto.UserResponseDto;
import com.antp.atmapplication.lib.AuthenticationException;
import com.antp.atmapplication.model.User;
import com.antp.atmapplication.security.AuthenticationService;
import com.antp.atmapplication.service.mapper.ResponseDtoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final ResponseDtoMapper<UserResponseDto, User> responseDtoMapper;

    public AuthenticationController(AuthenticationService authenticationService,
                                    ResponseDtoMapper<UserResponseDto, User> responseDtoMapper) {
        this.authenticationService = authenticationService;
        this.responseDtoMapper = responseDtoMapper;
    }

    @PostMapping("/register")
    public UserResponseDto register(@RequestBody UserRegistrationDto userRegistrationDto) {
        User user = authenticationService.register(userRegistrationDto.getName(),
                userRegistrationDto.getPassword());
        return responseDtoMapper.mapToDto(user);
    }

//    @PostMapping("/login")
//    public User login(@RequestBody UserLoginDto userLoginDto)
//            throws AuthenticationException {
//        User user = authenticationService.login(userLoginDto.getName(), userLoginDto.getPassword());
//        return user;
//    }
}
