package com.antp.atmapplication.service.mapper;

import com.antp.atmapplication.dto.UserResponseDto;
import com.antp.atmapplication.model.Account;
import com.antp.atmapplication.model.User;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserResponseDtoMapper implements ResponseDtoMapper<UserResponseDto, User> {
    @Override
    public UserResponseDto mapToDto(User model) {
        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(model.getId());
        userDto.setAccountIds(model.getAccounts().stream()
                .map(Account::getId).collect(Collectors.toList()));
        userDto.setRole(model.getRole());
        userDto.setName(model.getName());
        userDto.setPassword(model.getPassword());
        return userDto;
    }
}
