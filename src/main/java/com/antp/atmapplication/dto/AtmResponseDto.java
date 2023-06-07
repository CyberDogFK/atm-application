package com.antp.atmapplication.dto;

import java.util.List;

public record AtmResponseDto (
    Long id,
    String address,
    List<AtmBalanceResponseDto> atmBalanceDtoList
) {
}
