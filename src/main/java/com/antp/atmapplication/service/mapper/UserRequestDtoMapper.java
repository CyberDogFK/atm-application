package com.antp.atmapplication.service.mapper;

import com.antp.atmapplication.dto.UserRequestDto;
import com.antp.atmapplication.model.User;
import com.antp.atmapplication.service.AccountService;
import org.springframework.stereotype.Service;

@Service
public class UserRequestDtoMapper implements RequestDtoMapper<UserRequestDto, User> {
    private final AccountService accountService;

    public UserRequestDtoMapper(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public User mapToModel(UserRequestDto dto) {
        User user = new User();
        user.setAccounts(accountService.findAllByIds(dto.getAccountIds()));
        user.setRole(dto.getRole());
        user.setName(dto.getName());
        user.setPassword(dto.getPassword());
        return user;
    }
}
