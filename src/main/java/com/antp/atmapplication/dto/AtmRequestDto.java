package com.antp.atmapplication.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

public record AtmRequestDto (
        String address,
        List<Long> balanceIds
) {

}
