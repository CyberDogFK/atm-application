package com.antp.atmapplication.controller;

import com.antp.atmapplication.dto.AccountRequestDto;
import com.antp.atmapplication.dto.AccountResponseDto;
import com.antp.atmapplication.model.Account;
import com.antp.atmapplication.service.AccountService;
import com.antp.atmapplication.service.UserService;
import com.antp.atmapplication.service.mapper.RequestDtoMapper;
import com.antp.atmapplication.service.mapper.ResponseDtoMapper;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;
    private final ResponseDtoMapper<AccountResponseDto, Account> accountResponseDtoMapper;
    private final RequestDtoMapper<AccountRequestDto, Account> accountRequestDtoMapper;
    private final UserService userService;

    public AccountController(AccountService accountService,
                             ResponseDtoMapper<AccountResponseDto, Account> accountResponseDtoMapper,
                             RequestDtoMapper<AccountRequestDto, Account> accountRequestDtoMapper,
                             UserService userService) {
        this.accountService = accountService;
        this.accountResponseDtoMapper = accountResponseDtoMapper;
        this.accountRequestDtoMapper = accountRequestDtoMapper;
        this.userService = userService;
    }

    @GetMapping
    public List<AccountResponseDto> getAll() {
        return accountService.getAll().stream()
                .map(accountResponseDtoMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AccountResponseDto getById(@PathVariable Long id) {
        return accountResponseDtoMapper.mapToDto(
                accountService.findById(id)
        );
    }

    @PostMapping
    public AccountResponseDto create(@RequestBody AccountRequestDto accountRequestDto) {
        return accountResponseDtoMapper.mapToDto(accountService.save(
                accountRequestDtoMapper.mapToModel(accountRequestDto))
        );
    }

    @PutMapping("/{id}")
    public AccountResponseDto update(@PathVariable Long id) {
        Account account = accountService.findById(id);
        account.setId(id);
        return accountResponseDtoMapper.mapToDto(
                accountService.save(account)
        );
    }

    @PutMapping("/{id}/transfer/{sendToAccountId}")
    public AccountResponseDto updatePut(Authentication authentication,
                                        @PathVariable Long id,
                                        @PathVariable Long sendToAccountId,
                                        @RequestParam BigDecimal value) {
        Account userAccount = userService.findUserFromAuthentication(authentication)
                .getAccounts().stream()
                .filter(it -> it.getId().equals(id))
                .findFirst().orElseThrow(() ->
                        new RuntimeException("Can't find user "
                                + authentication.getName()
                                + " account with id " + id));
        Account sendToAccount = accountService.findById(sendToAccountId);
        if (!userAccount.getCurrency().getShortName()
                .equals(sendToAccount.getCurrency().getShortName())) {
            throw new RuntimeException("For now we are not support transferring between different currency");
        }
        return accountResponseDtoMapper.mapToDto(
                accountService.transferMoney(userAccount, sendToAccount, value)
        );
    }
}
