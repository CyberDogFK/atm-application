package com.antp.atmapplication.service;

import com.antp.atmapplication.model.AtmBalance;

import java.util.List;

public interface AtmBalanceService {
    List<AtmBalance> findAll();

    List<AtmBalance> findAllByIds(List<Long> ids);

    AtmBalance findById(Long id);

    AtmBalance save(AtmBalance atmBalance);
}
