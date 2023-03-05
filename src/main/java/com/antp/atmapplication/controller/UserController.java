package com.antp.atmapplication.controller;

import com.antp.atmapplication.dto.UserRequestDto;
import com.antp.atmapplication.dto.UserResponseDto;
import com.antp.atmapplication.model.User;
import com.antp.atmapplication.service.UserService;
import com.antp.atmapplication.service.mapper.RequestDtoMapper;
import com.antp.atmapplication.service.mapper.ResponseDtoMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final ResponseDtoMapper<UserResponseDto, User> responseDtoMapper;
    private final RequestDtoMapper<UserRequestDto, User> requestDtoMapper;

    public UserController(UserService userService,
                          ResponseDtoMapper<UserResponseDto, User> responseDtoMapper,
                          RequestDtoMapper<UserRequestDto, User> requestDtoMapper) {
        this.userService = userService;
        this.responseDtoMapper = responseDtoMapper;
        this.requestDtoMapper = requestDtoMapper;
    }

    @GetMapping
    public List<UserResponseDto> getAll() {
        return userService.getAll().stream()
                .map(responseDtoMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserResponseDto getById(@PathVariable Long id) {
        return responseDtoMapper.mapToDto(userService.getById(id));
    }

    @PutMapping("/{id}")
    public UserResponseDto update(@PathVariable Long id,
                                  @RequestBody UserRequestDto userDto) {
        User user = requestDtoMapper.mapToModel(userDto);
        user.setId(id);
        return responseDtoMapper.mapToDto(userService.save(user));
    }
}
