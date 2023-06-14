package com.antp.atmapplication.controller;

import com.antp.atmapplication.dto.AtmBalanceRequestDto;
import com.antp.atmapplication.dto.AtmBalanceResponseDto;
import com.antp.atmapplication.model.AtmBalance;
import com.antp.atmapplication.service.AccountService;
import com.antp.atmapplication.service.AtmBalanceService;
import com.antp.atmapplication.service.UserService;
import com.antp.atmapplication.service.mapper.RequestDtoMapper;
import com.antp.atmapplication.service.mapper.ResponseDtoMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import java.util.List;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/atm-balance")
@Tag(name = "Atm Balance Controller")
public class AtmBalanceController {
    private final AtmBalanceService atmBalanceService;
    private final ResponseDtoMapper<AtmBalanceResponseDto, AtmBalance> atmBalanceResponseDtoMapper;
    private final RequestDtoMapper<AtmBalanceRequestDto, AtmBalance> atmBalanceRequestDtoMapper;
    private final UserService userService;
    private final AccountService accountService;

    public AtmBalanceController(AtmBalanceService atmBalanceService,
                                ResponseDtoMapper<AtmBalanceResponseDto, AtmBalance> atmBalanceResponseDtoMapper,
                                RequestDtoMapper<AtmBalanceRequestDto, AtmBalance> atmBalanceRequestDtoMapper,
                                UserService userService,
                                AccountService accountService) {
        this.atmBalanceService = atmBalanceService;
        this.atmBalanceResponseDtoMapper = atmBalanceResponseDtoMapper;
        this.atmBalanceRequestDtoMapper = atmBalanceRequestDtoMapper;
        this.userService = userService;
        this.accountService = accountService;
    }

    @GetMapping
    @Operation(summary = "Get all atm balances")
    public List<AtmBalanceResponseDto> findAll() {
        return atmBalanceService.findAll().stream()
                .map(atmBalanceResponseDtoMapper::mapToDto)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find atm balance with id")
    public AtmBalanceResponseDto findById(@PathVariable Long id) {
        return atmBalanceResponseDtoMapper.mapToDto(
                atmBalanceService.getById(id)
        );
    }

    @PostMapping
    @Operation(summary = "Create atm balance")
    public AtmBalanceResponseDto add(@RequestBody AtmBalanceRequestDto dto) {
        return atmBalanceResponseDtoMapper.mapToDto(
                atmBalanceService.save(
                        atmBalanceRequestDtoMapper.mapToModel(dto))
        );
    }

    @PutMapping("/{id}/account/put/{accountId}")
    @Transactional
    @Operation(summary = "Put money in account from atm",
            description = """
    <p><b>User must be authenticated!</b></p>
    <p>
        First of all check banknotes. Atm can handle banknotes with value 100, 200, 500
        If not, return error.
        After that find account of user, and atm balance.
        And replenishes the atm balance and user account.
    </p>
    <p><b>WARNING!!!</b><p>
    <p>Operation is not atomic and not transactional,
    so be carefully before it will be changed</p>
    """)
    public AtmBalanceResponseDto putMoneyInAccount(@PathVariable
                                                       @Parameter(name = "Atm balance id")
                                                       Long id,
                                                @PathVariable
                                                       @Parameter(name = "Account id")
                                                       Long accountId,
                                                @RequestParam
                                                       @Parameter(name = "Value to transfer")
                                                       BigDecimal value,
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
    @Operation(summary = "Withdraw money from atm and account",
            description =
                    """
                    <p><b>User must be authenticated</b></p>
                    <p>
                        First of all check banknotes. Atm can handle banknotes with value 100, 200, 500
                        If not, return error.
                        After that find account of user, and atm balance.
                        And get money from user account and atm balance
                    </p>
                    <p><b>WARNING!!!</b><p>
                    <p>Operation is not atomic and not transactional,
                        so be carefully before it will be changed</p>
                    """)
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
