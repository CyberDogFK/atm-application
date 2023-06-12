package com.antp.atmapplication.controller;

import com.antp.atmapplication.dto.AtmRequestDto;
import com.antp.atmapplication.dto.AtmResponseDto;
import com.antp.atmapplication.exception.DataProcessingException;
import com.antp.atmapplication.model.Atm;
import com.antp.atmapplication.model.AtmBalance;
import com.antp.atmapplication.service.AtmBalanceService;
import com.antp.atmapplication.service.AtmService;
import com.antp.atmapplication.service.mapper.RequestDtoMapper;
import com.antp.atmapplication.service.mapper.ResponseDtoMapper;
import java.math.BigDecimal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/atm")
public class AtmController {
    private final static Logger logger = LoggerFactory.getLogger(AtmController.class);
    private final AtmService atmService;
    private final ResponseDtoMapper<AtmResponseDto, Atm> atmResponseDtoMapper;
    private final RequestDtoMapper<AtmRequestDto, Atm> atmRequestDtoMapper;

    public AtmController(AtmService atmService,
                         ResponseDtoMapper<AtmResponseDto, Atm> atmResponseDtoMapper,
                         RequestDtoMapper<AtmRequestDto, Atm> atmRequestDtoMapper) {
        this.atmService = atmService;
        this.atmResponseDtoMapper = atmResponseDtoMapper;
        this.atmRequestDtoMapper = atmRequestDtoMapper;
    }

    @GetMapping
    public List<AtmResponseDto> getAllAtm() {
        return atmService.getAll().stream()
                .map(atmResponseDtoMapper::mapToDto)
                .toList();
    }

    @GetMapping("/{id}")
    public AtmResponseDto getById(@PathVariable Long id) {
        return atmResponseDtoMapper.mapToDto(atmService.getById(id));
    }

    @PostMapping
    public AtmResponseDto createAtm(@RequestBody AtmRequestDto atmRequestDto) {
        return atmResponseDtoMapper.mapToDto(
                atmService.save(
                        atmRequestDtoMapper.mapToModel(atmRequestDto)
                )
        );
    }

    @PutMapping("/{id}")
    public AtmResponseDto addValueToAtm(@PathVariable Long id,
                                        @RequestParam String currency,
                                        @RequestParam Integer value) {
        Atm byId = atmService.getById(id);
        AtmBalance atmBalance = byId.getBalanceList().stream()
                .filter(it -> it.getCurrency().getShortName().equals(currency))
                .findFirst().orElseThrow(() ->
                        new DataProcessingException("Can't find any currency " + currency));
        BigDecimal balance = atmBalance.getBalance();
        logger.info("atm balance before" + balance);
        atmBalance.setBalance(balance.add(new BigDecimal(value)));
        return atmResponseDtoMapper.mapToDto(atmService.save(byId));
    }
}
