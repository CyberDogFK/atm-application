package com.antp.atmapplication.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

public record AccountResponseDto(
        Long id,
        Long userId,
        Long currencyId,
        BigDecimal balance
) {}
