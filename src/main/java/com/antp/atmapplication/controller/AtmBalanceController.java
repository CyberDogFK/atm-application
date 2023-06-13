package com.antp.atmapplication.controller;

import com.antp.atmapplication.dto.AccountResponseDto;
import com.antp.atmapplication.dto.AtmBalanceRequestDto;
import com.antp.atmapplication.dto.AtmBalanceResponseDto;
import com.antp.atmapplication.model.Account;
import com.antp.atmapplication.model.AtmBalance;
import com.antp.atmapplication.model.User;
import com.antp.atmapplication.service.AccountService;
import com.antp.atmapplication.service.AtmBalanceService;
import com.antp.atmapplication.service.AtmService;
import com.antp.atmapplication.service.UserService;
import com.antp.atmapplication.service.mapper.RequestDtoMapper;
import com.antp.atmapplication.service.mapper.ResponseDtoMapper;

import java.math.BigDecimal;
import java.util.List;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/atm-balance")
public class AtmBalanceController {
    private final static Logger logger = LoggerFactory.getLogger(AtmBalanceController.class);
    private final AtmBalanceService atmBalanceService;
    private final ResponseDtoMapper<AtmBalanceResponseDto, AtmBalance> atmBalanceResponseDtoMapper;
    private final RequestDtoMapper<AtmBalanceRequestDto, AtmBalance> atmBalanceRequestDtoMapper;
    private final ResponseDtoMapper<AccountResponseDto, Account> accountResponseDtoMapper;
    private final AtmService atmService;
    private final UserService userService;
    private final AccountService accountService;

    public AtmBalanceController(AtmBalanceService atmBalanceService,
                                ResponseDtoMapper<AtmBalanceResponseDto, AtmBalance> atmBalanceResponseDtoMapper,
                                RequestDtoMapper<AtmBalanceRequestDto, AtmBalance> atmBalanceRequestDtoMapper, ResponseDtoMapper<AccountResponseDto, Account> accountResponseDtoMapper, AtmService atmService, UserService userService, AccountService accountService) {
        this.atmBalanceService = atmBalanceService;
        this.atmBalanceResponseDtoMapper = atmBalanceResponseDtoMapper;
        this.atmBalanceRequestDtoMapper = atmBalanceRequestDtoMapper;
        this.accountResponseDtoMapper = accountResponseDtoMapper;
        this.atmService = atmService;
        this.userService = userService;
        this.accountService = accountService;
    }

    @GetMapping
    public List<AtmBalanceResponseDto> findAll() {
        return atmBalanceService.findAll().stream()
                .map(atmBalanceResponseDtoMapper::mapToDto)
                .toList();
    }

    @GetMapping("/{id}")
    public AtmBalanceResponseDto findById(@PathVariable Long id) {
        return atmBalanceResponseDtoMapper.mapToDto(
                atmBalanceService.getById(id)
        );
    }

    @PostMapping
    public AtmBalanceResponseDto add(@RequestBody AtmBalanceRequestDto dto) {
        return atmBalanceResponseDtoMapper.mapToDto(
                atmBalanceService.save(
                        atmBalanceRequestDtoMapper.mapToModel(dto))
        );
    }

    @PutMapping("/{id}/account/put/{accountId}")
    @Transactional
    public AtmBalanceResponseDto putMoneyInAccount(@PathVariable Long id,
                                                @PathVariable Long accountId,
                                                @RequestParam BigDecimal value,
                                                Authentication authentication) {
        checkBanknotes(value);
        final var account = accountService.findUserAccountById(
                userService.findUserFromAuthentication(authentication),
                accountId);
        final var atmBalance = atmBalanceService.getById(id);
        return atmBalanceResponseDtoMapper.mapToDto(
                atmBalanceService.puyMoneyIntoAccount(atmBalance, account, value)
        );
    }

    @PutMapping("/{id}/account/withdraw/{accountId}")
    @Transactional
    public AtmBalanceResponseDto withdrawFromAccount(@PathVariable Long id,
                                                  @PathVariable Long accountId,
                                                  @RequestParam BigDecimal value,
                                                  Authentication authentication) {
        checkBanknotes(value);
        final var account = accountService.findUserAccountById(
                userService.findUserFromAuthentication(authentication),
                accountId);
        final var atmBalance = atmBalanceService.getById(id);
        return atmBalanceResponseDtoMapper.mapToDto(
                atmBalanceService.withdrawMoneyIntoAccount(atmBalance, account, value)
        );
    }

    private void checkBanknotes(BigDecimal value) {
        if ((value.compareTo(new BigDecimal(100)) != 0)
                && (value.compareTo(new BigDecimal(200)) != 0)
                && (value.compareTo(new BigDecimal(500)) != 0)) {
            throw new RuntimeException("ATM can take only 100, 200, 500 banknotes");
        }
    }
}
