package com.antp.atmapplication.controller;

import com.antp.atmapplication.dto.AccountRequestDto;
import com.antp.atmapplication.dto.AccountResponseDto;
import com.antp.atmapplication.model.Account;
import com.antp.atmapplication.service.AccountService;
import com.antp.atmapplication.service.mapper.RequestDtoMapper;
import com.antp.atmapplication.service.mapper.ResponseDtoMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;
    private final ResponseDtoMapper<AccountResponseDto, Account> accountResponseDtoMapper;
    private final RequestDtoMapper<AccountRequestDto, Account> accountRequestDtoMapper;

    public AccountController(AccountService accountService,
                             ResponseDtoMapper<AccountResponseDto, Account> accountResponseDtoMapper,
                             RequestDtoMapper<AccountRequestDto, Account> accountRequestDtoMapper) {
        this.accountService = accountService;
        this.accountResponseDtoMapper = accountResponseDtoMapper;
        this.accountRequestDtoMapper = accountRequestDtoMapper;
    }

    @GetMapping
    public List<AccountResponseDto> getAll() {
        return accountService.getAll().stream()
                .map(accountResponseDtoMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public AccountResponseDto create(@RequestBody AccountRequestDto accountRequestDto) {
        return accountResponseDtoMapper.mapToDto(accountService.save(
                accountRequestDtoMapper.mapToModel(accountRequestDto)));
    }
}
