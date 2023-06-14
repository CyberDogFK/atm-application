package com.antp.atmapplication.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountRequestDto {
    private Long userId;
    private Long currencyId;
    private BigDecimal balance;
}
