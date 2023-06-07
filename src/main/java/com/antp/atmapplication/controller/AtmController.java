package com.antp.atmapplication.controller;

import com.antp.atmapplication.dto.AtmRequestDto;
import com.antp.atmapplication.dto.AtmResponseDto;
import com.antp.atmapplication.model.Atm;
import com.antp.atmapplication.service.AtmBalanceService;
import com.antp.atmapplication.service.AtmService;
import com.antp.atmapplication.service.mapper.ResponseDtoMapper;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/atm")
public class AtmController {
    private final AtmService atmService;
    private final AtmBalanceService atmBalanceService;
    private final ResponseDtoMapper<AtmResponseDto, Atm> atmResponseDtoMapper;

    public AtmController(AtmService atmService,
                         AtmBalanceService atmBalanceService,
                         ResponseDtoMapper<AtmResponseDto, Atm> atmResponseDtoMapper) {
        this.atmService = atmService;
        this.atmBalanceService = atmBalanceService;
        this.atmResponseDtoMapper = atmResponseDtoMapper;
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
        return atmResponseDtoMapper.mapToDto(atmService.save(new Atm(atmRequestDto.address(),
                atmBalanceService.findAllByIds(atmRequestDto.balanceIds()))));
    }
}
