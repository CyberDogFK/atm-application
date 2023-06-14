package com.antp.atmapplication.controller;

import com.antp.atmapplication.dto.AccountRequestDto;
import com.antp.atmapplication.dto.AccountResponseDto;
import com.antp.atmapplication.model.Account;
import com.antp.atmapplication.service.AccountService;
import com.antp.atmapplication.service.UserService;
import com.antp.atmapplication.service.mapper.RequestDtoMapper;
import com.antp.atmapplication.service.mapper.ResponseDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
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
@Tag(name = "Account controller", description = "Allow control and work with accounts")
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
    @Operation(summary = "Get all accounts")
    public List<AccountResponseDto> getAll() {
        return accountService.getAll().stream()
                .map(accountResponseDtoMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get account with id")
    public AccountResponseDto getById(@PathVariable
                                          @Parameter(name = "Account id")
                                          Long id) {
        return accountResponseDtoMapper.mapToDto(
                accountService.findById(id)
        );
    }

    @PostMapping
    @Operation(summary = "Create new account")
    public AccountResponseDto create(@RequestBody AccountRequestDto accountRequestDto) {
        return accountResponseDtoMapper.mapToDto(accountService.save(
                accountRequestDtoMapper.mapToModel(accountRequestDto))
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update existing account")
    public AccountResponseDto update(@PathVariable
                                         @Parameter(name = "Account id")
                                         Long id) {
        Account account = accountService.findById(id);
        account.setId(id);
        return accountResponseDtoMapper.mapToDto(
                accountService.save(account)
        );
    }

    @PutMapping("/{id}/transfer/{sendToAccountId}")
    @Operation(summary = "Transfer money from account to another",
            description = """
                    <b>User must be authenticated!</b>
                    <p>Take specified user account and check it on behavior,
                     and specified another account, check it currency<br></p>
                    <b>!!!Warning!!!</b><br>
                    <ul>
                    <li>For now we are not support transferring between different currency</li>
                    <li>Operation is not atomic, and not transactional, plan to add this in future updates</li>
                    </ul>
                    """)
    @Transactional
    public AccountResponseDto updatePut(Authentication authentication,
                                        @PathVariable
                                        @Parameter(name = "User account",
                                                description = "Id of account, "
                                                        + "from transferring money")
                                        Long id,
                                        @PathVariable
                                        @Parameter(name = "Other account",
                                                description = "Id of account,"
                                                        + " to transferring money" )
                                        Long sendToAccountId,
                                        @RequestParam
                                        @Parameter(name = "Transferring value")
                                        BigDecimal value) {
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
            throw new RuntimeException("For now we are not support "
                    + "transferring between different currency");
        }
        return accountResponseDtoMapper.mapToDto(
                accountService.transferMoney(userAccount, sendToAccount, value)
        );
    }
}
