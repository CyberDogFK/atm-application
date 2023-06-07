package com.antp.atmapplication.service.mapper;

import com.antp.atmapplication.dto.AccountResponseDto;
import com.antp.atmapplication.model.Account;
import org.springframework.stereotype.Service;

@Service
public class AccountResponseDtoMapper implements ResponseDtoMapper<AccountResponseDto, Account> {
    @Override
    public AccountResponseDto mapToDto(Account model) {
        return new AccountResponseDto(
                model.getId(),
                model.getUser().getId(),
                model.getCurrency().getId(),
                model.getBalance()
        );
    }
}
