package com.antp.atmapplication.service;

import com.antp.atmapplication.model.Account;
import com.antp.atmapplication.model.AtmBalance;

import java.math.BigDecimal;
import java.util.List;

public interface AtmBalanceService {
    List<AtmBalance> findAll();

    List<AtmBalance> findAllByIds(List<Long> ids);

    AtmBalance getById(Long id);

    AtmBalance save(AtmBalance atmBalance);

    AtmBalance withdrawMoneyIntoAccount(AtmBalance atmBalance, Account account, BigDecimal value);

    AtmBalance puyMoneyIntoAccount(AtmBalance atmBalance, Account account, BigDecimal value);
}
