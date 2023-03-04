package com.antp.atmapplication.controller;

import com.antp.atmapplication.dto.AccountResponseDto;
import com.antp.atmapplication.model.Account;
import com.antp.atmapplication.service.AccountService;
import com.antp.atmapplication.service.mapper.AccountResponseDtoMapper;
import com.antp.atmapplication.service.mapper.ResponseDtoMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;
    private final ResponseDtoMapper<AccountResponseDto, Account> accountResponseDtoMapper;

    public AccountController(AccountService accountService,
                             ResponseDtoMapper<AccountResponseDto, Account> accountResponseDtoMapper) {
        this.accountService = accountService;
        this.accountResponseDtoMapper = accountResponseDtoMapper;
    }

    @GetMapping
    public List<AccountResponseDto> getAll() {
        return accountService.getAll().stream()
                .map(accountResponseDtoMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
