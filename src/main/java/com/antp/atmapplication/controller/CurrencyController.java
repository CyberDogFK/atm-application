package com.antp.atmapplication.controller;

import com.antp.atmapplication.dto.CurrencyRequestDto;
import com.antp.atmapplication.dto.CurrencyResponseDto;
import com.antp.atmapplication.model.Currency;
import com.antp.atmapplication.service.CurrencyService;
import com.antp.atmapplication.service.mapper.RequestDtoMapper;
import com.antp.atmapplication.service.mapper.ResponseDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/currency")
@Tag(name = "Currency controller")
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
    @Operation(summary = "Get all available currency")
    public List<CurrencyResponseDto> getAll() {
        return currencyService.getAll().stream()
                .map(currencyResponseDtoMapper::mapToDto)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get currency with specific id")
    public CurrencyResponseDto getById(@PathVariable
                                           @Parameter(name = "Currency id")
                                           Long id) {
        return currencyResponseDtoMapper.mapToDto(
                currencyService.getById(id)
        );
    }

    @PostMapping
    @Operation(summary = "Create specific currency")
    public CurrencyResponseDto create(@RequestBody CurrencyRequestDto dto) {
        return currencyResponseDtoMapper.mapToDto(
                currencyService.save(
                        currencyRequestDtoMapper.mapToModel(dto)
                )
        );
    }
}
