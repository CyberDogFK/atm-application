package com.antp.atmapplication.controller;

import com.antp.atmapplication.dto.AccountRequestDto;
import com.antp.atmapplication.dto.AccountResponseDto;
import com.antp.atmapplication.model.Account;
import com.antp.atmapplication.service.AccountService;
import com.antp.atmapplication.service.UserService;
import com.antp.atmapplication.service.mapper.RequestDtoMapper;
import com.antp.atmapplication.service.mapper.ResponseDtoMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

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
        Account userAccount = userService.findByName(authentication.getName()).orElseThrow(() ->
                        new RuntimeException("Can't find user with name " + authentication.getName()))
                .getAccounts().stream()
                .filter(it -> it.getId().equals(id))
                .findFirst().orElseThrow(() ->
                        new RuntimeException("Can't find user "
                                + authentication.getName()
                                + " account with id " + id));
        Account sendToAccount = accountService.findById(sendToAccountId);
        return accountResponseDtoMapper.mapToDto(
                accountService.transferMoney(userAccount, sendToAccount, value));
    }

//    @PutMapping("/{id}")
//    public AccountResponseDto updateWithdraw() {}
}
