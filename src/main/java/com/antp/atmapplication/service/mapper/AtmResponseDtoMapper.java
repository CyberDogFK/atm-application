package com.antp.atmapplication.service.mapper;

import com.antp.atmapplication.dto.AtmBalanceResponseDto;
import com.antp.atmapplication.dto.AtmResponseDto;
import com.antp.atmapplication.model.Atm;
import org.springframework.stereotype.Service;

@Service
public class AtmResponseDtoMapper implements ResponseDtoMapper<AtmResponseDto, Atm> {
    @Override
    public AtmResponseDto mapToDto(Atm model) {
        return new AtmResponseDto(model.getId(),
                model.getAddress(),
                model.getBalanceList().stream().map(it -> new AtmBalanceResponseDto(
                        it.getId(),
                        it.getAtm().getId(),
                        it.getCurrency().getId(),
                        it.getBalance()
                )).toList()
        );
    }
}
