package com.antp.atmapplication.service.mapper;

import com.antp.atmapplication.dto.AccountRequestDto;
import com.antp.atmapplication.model.Account;
import com.antp.atmapplication.service.CurrencyService;
import com.antp.atmapplication.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class AccountRequestDtoMapper implements RequestDtoMapper<AccountRequestDto, Account> {
    private final CurrencyService currencyService;
    private final UserService userService;

    public AccountRequestDtoMapper(CurrencyService currencyService,
                                   UserService userService) {
        this.currencyService = currencyService;
        this.userService = userService;
    }

    @Override
    public Account mapToModel(AccountRequestDto dto) {
        return new Account(
                userService.getById(dto.getUserId()),
                currencyService.getById(dto.getCurrencyId()),
                dto.getBalance()
        );
    }
}
