package com.antp.atmapplication.service.mapper;

import com.antp.atmapplication.dto.AtmRequestDto;
import com.antp.atmapplication.model.Atm;
import com.antp.atmapplication.model.AtmBalance;
import com.antp.atmapplication.service.AtmBalanceService;
import com.antp.atmapplication.service.AtmService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AtmRequestDtoMapper implements RequestDtoMapper<AtmRequestDto, Atm> {
    private final AtmBalanceService atmBalanceService;

    public AtmRequestDtoMapper(AtmBalanceService atmBalanceService) {
        this.atmBalanceService = atmBalanceService;
    }

    @Override
    public Atm mapToModel(AtmRequestDto dto) {
        return new Atm(
                dto.address(),
                findByIdsOrEmpty(dto.balanceIds())
        );
    }

    private List<AtmBalance> findByIdsOrEmpty(List<Long> balanceIds) {
        if (balanceIds.isEmpty()) {
            return List.of();
        }
        return atmBalanceService.findAllByIds(balanceIds);
    }
}
