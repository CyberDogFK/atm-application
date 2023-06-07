package com.antp.atmapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class AtmBalanceResponseDto {
    private Long id;
    private Long atmId;
    private Long currencyId;
    private BigDecimal balance;
}
