package com.antp.atmapplication.controller;

import com.antp.atmapplication.dto.CurrencyRequestDto;
import com.antp.atmapplication.dto.CurrencyResponseDto;
import com.antp.atmapplication.model.Currency;
import com.antp.atmapplication.service.CurrencyService;
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
@RequestMapping("/currency")
public class CurrencyController {
    private final CurrencyService currencyService;
    private final ResponseDtoMapper<CurrencyResponseDto, Currency> currencyResponseDtoMapper;
    private final RequestDtoMapper<CurrencyRequestDto, Currency> currencyRequestDtoMapper;

    public CurrencyController(CurrencyService currencyService,
                              ResponseDtoMapper<CurrencyResponseDto, Currency> currencyResponseDtoMapper,
                              RequestDtoMapper<CurrencyRequestDto, Currency> currencyRequestDtoMapper) {
        this.currencyService = currencyService;
        this.currencyResponseDtoMapper = currencyResponseDtoMapper;
        this.currencyRequestDtoMapper = currencyRequestDtoMapper;
    }

    @GetMapping
    public List<CurrencyResponseDto> getAll() {
        return currencyService.getAll().stream()
                .map(currencyResponseDtoMapper::mapToDto)
                .toList();
    }

    @GetMapping("/{id}")
    public CurrencyResponseDto getById(@PathVariable Long id) {
        return currencyResponseDtoMapper.mapToDto(
                currencyService.getById(id)
        );
    }

    @PostMapping
    public CurrencyResponseDto create(@RequestBody CurrencyRequestDto dto) {
        return currencyResponseDtoMapper.mapToDto(
                currencyService.save(
                        new Currency(
                                dto.getShortName(),
                                dto.getName()))
        );
    }
}
