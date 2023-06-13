package com.antp.atmapplication.controller;

import com.antp.atmapplication.dto.*;
import com.antp.atmapplication.exception.DataProcessingException;
import com.antp.atmapplication.model.Account;
import com.antp.atmapplication.model.Atm;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/atm")
public class AtmController {
    private final static Logger logger = LoggerFactory.getLogger(AtmController.class);
    private final AtmService atmService;
    private final UserService userService;
    private final AccountService accountService;
    private final AtmBalanceService atmBalanceService;
    private final ResponseDtoMapper<AtmResponseDto, Atm> atmResponseDtoMapper;
    private final RequestDtoMapper<AtmRequestDto, Atm> atmRequestDtoMapper;
    private final ResponseDtoMapper<AtmBalanceResponseDto, AtmBalance> atmBalanceResponseDtoMapper;
    private final ResponseDtoMapper<AccountResponseDto, Account> accountResponseDtoMapper;

    public AtmController(AtmService atmService,
                         UserService userService, AccountService accountService, AtmBalanceService atmBalanceService, ResponseDtoMapper<AtmResponseDto, Atm> atmResponseDtoMapper,
                         RequestDtoMapper<AtmRequestDto, Atm> atmRequestDtoMapper,
                         ResponseDtoMapper<AtmBalanceResponseDto, AtmBalance> atmBalanceResponseDtoMapper, ResponseDtoMapper<AccountResponseDto, Account> accountResponseDtoMapper) {
        this.atmService = atmService;
        this.userService = userService;
        this.accountService = accountService;
        this.atmBalanceService = atmBalanceService;
        this.atmResponseDtoMapper = atmResponseDtoMapper;
        this.atmRequestDtoMapper = atmRequestDtoMapper;
        this.atmBalanceResponseDtoMapper = atmBalanceResponseDtoMapper;
        this.accountResponseDtoMapper = accountResponseDtoMapper;
    }

    @GetMapping
    public List<AtmResponseDto> getAllAtm() {
        return atmService.getAll().stream()
                .map(atmResponseDtoMapper::mapToDto)
                .toList();
    }

    @GetMapping("/{id}")
    public AtmResponseDto getById(@PathVariable Long id) {
        return atmResponseDtoMapper.mapToDto(atmService.getById(id));
    }

    @PostMapping
    public AtmResponseDto createAtm(@RequestBody AtmRequestDto atmRequestDto) {
        return atmResponseDtoMapper.mapToDto(
                atmService.save(
                        atmRequestDtoMapper.mapToModel(atmRequestDto)
                )
        );
    }

    @PutMapping("{id}")
    public AtmResponseDto update(@RequestBody AtmRequestDto atmRequestDto, @PathVariable Long id) {
        Atm atm = atmRequestDtoMapper.mapToModel(atmRequestDto);
        atm.setId(id);
        return atmResponseDtoMapper.mapToDto(atmService.save(atm));
    }

    @PutMapping("/{id}/put")
    @Transactional
    public AtmResponseDto addValueToAtm(@PathVariable Long id,
                                        @RequestParam String currency,
                                        @RequestParam Integer value) {
        Atm byId = atmService.getById(id);
        AtmBalance atmBalance = byId.getBalanceList().stream()
                .filter(it -> it.getCurrency().getShortName().equals(currency))
                .findFirst().orElseThrow(() ->
                        new DataProcessingException("Can't find any currency " + currency));
        BigDecimal balance = atmBalance.getBalance();
        logger.info("atm balance before" + balance);
        atmBalance.setBalance(balance.add(new BigDecimal(value)));
        return atmResponseDtoMapper.mapToDto(atmService.save(byId));
    }

    @GetMapping("/{id}/balances")
    public List<AtmBalanceResponseDto> getAtmBalances(@PathVariable Long id) {
        return atmService.getById(id)
                .getBalanceList().stream()
                .map(atmBalanceResponseDtoMapper::mapToDto)
                .toList();
    }

    @PutMapping("/{id}/account/put/{accountId}")
    @Transactional
    public AccountResponseDto putMoneyInAccount(@PathVariable Long id,
                                                @PathVariable Long accountId,
                                                @RequestParam String currency,
                                                @RequestParam BigDecimal value,
                                                Authentication authentication) {
        User user = userService.findByName(authentication.getName()).orElseThrow(() ->
                new RuntimeException("Can't find your account with name "
                        + authentication.getName()));
        Account account = user.getAccounts().stream()
                .filter(it -> it.getId().equals(accountId))
                .findFirst().orElseThrow(() ->
                        new RuntimeException("Can't find user: " + user.getName()
                                + " account with id " + accountId));
        Atm atm = atmService.getById(id);
        AtmBalance atmBalance = atm.getBalanceList().stream()
                .filter(it -> it.getCurrency().getShortName().equals(currency))
                .findFirst().orElseThrow(() ->
                        new RuntimeException("Atm with id " + atm.getId()
                                + " have only currency " + atm.getBalanceList()));
        account.setBalance(account.getBalance().add(value));
        atmBalance.setBalance(atmBalance.getBalance().add(value));
        atmBalanceService.save(atmBalance);
        Account save = accountService.save(account);
        return accountResponseDtoMapper.mapToDto(save);
    }

    @PutMapping("/{id}/account/withdraw/{accountId}")
    @Transactional
    public AccountResponseDto withdrawFromAccount(@PathVariable Long id,
                                                  @PathVariable Long accountId,
                                                  @RequestParam String currency,
                                                  @RequestParam BigDecimal value,
                                                  Authentication authentication) {
        User user = userService.findByName(authentication.getName()).orElseThrow(() ->
                new RuntimeException("Can't find your account with name "
                        + authentication.getName()));
        Account account = user.getAccounts().stream()
                .filter(it -> it.getId().equals(accountId))
                .findFirst().orElseThrow(() ->
                        new RuntimeException("Can't find user: " + user.getName()
                                + " account with id " + accountId));
        Atm atm = atmService.getById(id);
        AtmBalance atmBalance = atm.getBalanceList().stream()
                .filter(it -> it.getCurrency().getShortName().equals(currency))
                .findFirst().orElseThrow(() ->
                        new RuntimeException("Atm with id " + atm.getId()
                                + " have only currency " + atm.getBalanceList()));
        account.setBalance(account.getBalance().subtract(value));
        atmBalance.setBalance(atmBalance.getBalance().subtract(value));
        if (account.getBalance().compareTo(new BigDecimal(0)) < 0) {
            throw new RuntimeException("On your account is not enough money, balance: "
                    + account.getBalance() );
        }
        if (atmBalance.getBalance().compareTo(new BigDecimal(0)) < 0) {
            throw new RuntimeException("Atm balance of "
                    + atmBalance.getCurrency()
                    + "is not enough");
        }
        Account save = accountService.save(account);
        atmBalanceService.save(atmBalance);
        return accountResponseDtoMapper.mapToDto(save);

    }
}
