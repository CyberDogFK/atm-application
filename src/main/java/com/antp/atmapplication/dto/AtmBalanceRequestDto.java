package com.antp.atmapplication.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AtmBalanceRequestDto {
    private Long atmId;
    private Long currencyId;
    private BigDecimal balance;
}
