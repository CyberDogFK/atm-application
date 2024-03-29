package com.antp.atmapplication.controller;

import com.antp.atmapplication.dto.AtmBalanceResponseDto;
import com.antp.atmapplication.dto.AtmRequestDto;
import com.antp.atmapplication.dto.AtmResponseDto;
import com.antp.atmapplication.exception.DataProcessingException;
import com.antp.atmapplication.model.Atm;
import com.antp.atmapplication.model.AtmBalance;
import com.antp.atmapplication.service.AtmService;
import com.antp.atmapplication.service.mapper.RequestDtoMapper;
import com.antp.atmapplication.service.mapper.ResponseDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import java.util.List;
import jakarta.transaction.Transactional;
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
@Tag(name = "Atm controller")
public class AtmController {
    private final static Logger logger = LoggerFactory.getLogger(AtmController.class);
    private final AtmService atmService;
    private final ResponseDtoMapper<AtmResponseDto, Atm> atmResponseDtoMapper;
    private final RequestDtoMapper<AtmRequestDto, Atm> atmRequestDtoMapper;
    private final ResponseDtoMapper<AtmBalanceResponseDto, AtmBalance> atmBalanceResponseDtoMapper;

    public AtmController(AtmService atmService,
                         ResponseDtoMapper<AtmResponseDto, Atm> atmResponseDtoMapper,
                         RequestDtoMapper<AtmRequestDto, Atm> atmRequestDtoMapper,
                         ResponseDtoMapper<AtmBalanceResponseDto, AtmBalance> atmBalanceResponseDtoMapper) {
        this.atmService = atmService;
        this.atmResponseDtoMapper = atmResponseDtoMapper;
        this.atmRequestDtoMapper = atmRequestDtoMapper;
        this.atmBalanceResponseDtoMapper = atmBalanceResponseDtoMapper;
    }

    @GetMapping
    @Operation(summary = "Get all atms")
    public List<AtmResponseDto> getAllAtm() {
        return atmService.getAll().stream()
                .map(atmResponseDtoMapper::mapToDto)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get atm with id")
    public AtmResponseDto getById(@PathVariable
                                      @Parameter(name = "Atm id") Long id) {
        return atmResponseDtoMapper.mapToDto(atmService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create new atm")
    public AtmResponseDto createAtm(@RequestBody AtmRequestDto atmRequestDto) {
        return atmResponseDtoMapper.mapToDto(
                atmService.save(
                        atmRequestDtoMapper.mapToModel(atmRequestDto)
                )
        );
    }

    @PutMapping("{id}")
    @Operation(summary = "Change existing atm")
    public AtmResponseDto update(@RequestBody AtmRequestDto atmRequestDto, @PathVariable Long id) {
        Atm atm = atmRequestDtoMapper.mapToModel(atmRequestDto);
        atm.setId(id);
        return atmResponseDtoMapper.mapToDto(atmService.save(atm));
    }

    @PutMapping("/{id}/put")
    @Transactional
    @Operation(summary = "Put to atm money",
            description = """
            <p>
            Check currency, what admin want to add,
            find account with those currency and add to it
            </p>
            """)
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

    @GetMapping("/{id}/balances")
    @Operation(summary = "Get balances of specific atm")
    public List<AtmBalanceResponseDto> getAtmBalances(@PathVariable Long id) {
        return atmService.getById(id)
                .getBalanceList().stream()
                .map(atmBalanceResponseDtoMapper::mapToDto)
                .toList();
    }
}
