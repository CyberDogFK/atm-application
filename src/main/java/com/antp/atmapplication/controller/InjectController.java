package com.antp.atmapplication.controller;

import com.antp.atmapplication.model.Account;
import com.antp.atmapplication.model.Atm;
import com.antp.atmapplication.model.AtmBalance;
import com.antp.atmapplication.model.Currency;
import com.antp.atmapplication.service.AccountService;
import com.antp.atmapplication.service.AtmBalanceService;
import com.antp.atmapplication.service.AtmService;
import com.antp.atmapplication.service.CurrencyService;
import com.antp.atmapplication.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class InjectController {
    private final CurrencyService currencyService;
    private final UserService userService;
    private final AccountService accountService;
    private final AtmBalanceService atmBalanceService;
    private final AtmService atmService;

    public InjectController(CurrencyService currencyService,
                            UserService userService,
                            AccountService accountService,
                            AtmBalanceService atmBalanceService,
                            AtmService atmService) {
        this.currencyService = currencyService;
        this.userService = userService;
        this.accountService = accountService;
        this.atmBalanceService = atmBalanceService;
        this.atmService = atmService;
    }

    @GetMapping("/inject")
    public String injectData() {
        final var currencyPrepared = new Currency("USD", "United States Dollar");
        final var currency = currencyService.save(currencyPrepared);
        final var userPrepared = userService.getById(1L);
        final var accountPrepared = new Account(userPrepared, currency, new BigDecimal(100000));
        final var account = accountService.save(accountPrepared);
        userPrepared.getAccounts().add(account);
        final var user = userService.save(userPrepared);
        final var atmBalancePrepared = new AtmBalance(currency, new BigDecimal(50000));
        final var atmBalance = atmBalanceService.save(atmBalancePrepared);
        final var atmPrepared = new Atm("Pravdy Ave.", List.of(atmBalance));
        final var atm = atmService.save(atmPrepared);
        return "True";
    }
}
