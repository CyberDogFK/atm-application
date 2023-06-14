package com.antp.atmapplication.service.mapper;

import com.antp.atmapplication.dto.CurrencyRequestDto;
import com.antp.atmapplication.model.Currency;
import org.springframework.stereotype.Service;

@Service
public class CurrencyRequestDtoMapper implements RequestDtoMapper<CurrencyRequestDto, Currency> {
    @Override
    public Currency mapToModel(CurrencyRequestDto dto) {
        return new Currency(dto.getShortName(), dto.getName());
    }
}
