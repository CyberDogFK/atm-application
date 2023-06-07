package com.antp.atmapplication.controller;

import com.antp.atmapplication.dto.AtmBalanceRequestDto;
import com.antp.atmapplication.dto.AtmBalanceResponseDto;
import com.antp.atmapplication.model.AtmBalance;
import com.antp.atmapplication.service.AtmBalanceService;
import com.antp.atmapplication.service.mapper.RequestDtoMapper;
import com.antp.atmapplication.service.mapper.ResponseDtoMapper;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/atm-balance")
public class AtmBalanceController {
    private final AtmBalanceService atmBalanceService;
    private final ResponseDtoMapper<AtmBalanceResponseDto, AtmBalance> atmBalanceResponseDtoMapper;
    private final RequestDtoMapper<AtmBalanceRequestDto, AtmBalance> atmBalanceRequestDtoMapper;

    public AtmBalanceController(AtmBalanceService atmBalanceService,
                                ResponseDtoMapper<AtmBalanceResponseDto, AtmBalance> atmBalanceResponseDtoMapper,
                                RequestDtoMapper<AtmBalanceRequestDto, AtmBalance> atmBalanceRequestDtoMapper) {
        this.atmBalanceService = atmBalanceService;
        this.atmBalanceResponseDtoMapper = atmBalanceResponseDtoMapper;
        this.atmBalanceRequestDtoMapper = atmBalanceRequestDtoMapper;
    }

    @GetMapping
    public List<AtmBalanceResponseDto> findAll() {
        return atmBalanceService.findAll().stream()
                .map(atmBalanceResponseDtoMapper::mapToDto)
                .toList();
    }

    @GetMapping("/{id}")
    public AtmBalanceResponseDto findById(@PathVariable Long id) {
        return atmBalanceResponseDtoMapper.mapToDto(
                atmBalanceService.findById(id)
        );
    }

    @PostMapping
    public AtmBalanceResponseDto add(@RequestBody AtmBalanceRequestDto dto) {
        return atmBalanceResponseDtoMapper.mapToDto(
                atmBalanceService.save(
                        atmBalanceRequestDtoMapper.mapToModel(dto))
        );
    }
}
