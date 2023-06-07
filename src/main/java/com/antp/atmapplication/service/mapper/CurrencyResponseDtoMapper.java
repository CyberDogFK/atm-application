package com.antp.atmapplication.service.mapper;

import com.antp.atmapplication.dto.CurrencyResponseDto;
import com.antp.atmapplication.model.Currency;
import org.springframework.stereotype.Service;

@Service
public class CurrencyResponseDtoMapper implements ResponseDtoMapper<CurrencyResponseDto, Currency> {
    @Override
    public CurrencyResponseDto mapToDto(Currency model) {
        return new CurrencyResponseDto(
                model.getId(),
                model.getShortName(),
                model.getName()
        );
    }
}
