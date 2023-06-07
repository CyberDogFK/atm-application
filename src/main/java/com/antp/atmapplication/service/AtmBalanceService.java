package com.antp.atmapplication.service;

import com.antp.atmapplication.model.AtmBalance;

import java.util.List;

public interface AtmBalanceService {
    List<AtmBalance> getAll();
    List<AtmBalance> findAllByIds(List<Long> ids);
}
