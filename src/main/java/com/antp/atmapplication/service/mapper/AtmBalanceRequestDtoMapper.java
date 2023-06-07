package com.antp.atmapplication.service.mapper;

import com.antp.atmapplication.dto.AtmBalanceRequestDto;
import com.antp.atmapplication.model.AtmBalance;
import com.antp.atmapplication.service.AtmService;
import com.antp.atmapplication.service.CurrencyService;
import org.springframework.stereotype.Service;

@Service
public class AtmBalanceRequestDtoMapper implements RequestDtoMapper<AtmBalanceRequestDto, AtmBalance> {
    private final AtmService atmService;
    private final CurrencyService currencyService;

    public AtmBalanceRequestDtoMapper(AtmService atmService, CurrencyService currencyService) {
        this.atmService = atmService;
        this.currencyService = currencyService;
    }

    @Override
    public AtmBalance mapToModel(AtmBalanceRequestDto dto) {
        return new AtmBalance(
                atmService.getById(dto.getAtmId()),
                currencyService.getById(dto.getCurrencyId()),
                dto.getBalance()
        );
    }
}
