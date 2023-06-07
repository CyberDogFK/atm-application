package com.antp.atmapplication.dto;

import java.math.BigDecimal;

public record AtmBalanceResponseDto(
        Long id,
        Long atmId,
        Long currencyId,
        BigDecimal balance
) {}
