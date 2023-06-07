package com.antp.atmapplication.service.mapper;

import com.antp.atmapplication.dto.AtmBalanceResponseDto;
import com.antp.atmapplication.model.AtmBalance;
import org.springframework.stereotype.Service;

@Service
public class AtmBalanceResponseDtoMapper implements ResponseDtoMapper<AtmBalanceResponseDto, AtmBalance> {
    @Override
    public AtmBalanceResponseDto mapToDto(AtmBalance model) {
        return new AtmBalanceResponseDto(
                model.getId(),
                model.getAtm().getId(),
                model.getCurrency().getId(),
                model.getBalance()
        );
    }
}
