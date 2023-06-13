package com.antp.atmapplication.service.impl;

import com.antp.atmapplication.exception.DataProcessingException;
import com.antp.atmapplication.model.Account;
import com.antp.atmapplication.model.AtmBalance;
import com.antp.atmapplication.repository.AtmBalanceRepository;
import com.antp.atmapplication.service.AccountService;
import com.antp.atmapplication.service.AtmBalanceService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AtmBalanceServiceImpl implements AtmBalanceService {
    private final AtmBalanceRepository atmBalanceRepository;
    private final AccountService accountService;

    public AtmBalanceServiceImpl(AtmBalanceRepository atmBalanceRepository,
                                 AccountService accountService) {
        this.atmBalanceRepository = atmBalanceRepository;
        this.accountService = accountService;
    }

    @Override
    public List<AtmBalance> findAll() {
        return atmBalanceRepository.findAll();
    }

    @Override
    public List<AtmBalance> findAllByIds(List<Long> ids) {
        return atmBalanceRepository.findAllById(ids);
    }

    @Override
    public AtmBalance getById(Long id) {
        return atmBalanceRepository.findById(id).orElseThrow(() ->
                new DataProcessingException("Can't find atm balance with id " + id));
    }

    @Override
    public AtmBalance save(AtmBalance atmBalance) {
        return atmBalanceRepository.save(atmBalance);
    }

    @Override
    @Transactional
    public AtmBalance withdrawMoneyIntoAccount(AtmBalance atmBalance, Account account, BigDecimal value) {
        final var prevAtmBalance = atmBalance.getBalance();
        final var prevAccountBalance = account.getBalance();
        account.setBalance(account.getBalance().subtract(value));
        atmBalance.setBalance(atmBalance.getBalance().subtract(value));
        if (account.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("On your account is not enough money, balance: "
                    + prevAtmBalance);
        }
        if (atmBalance.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Atm balance of "
                    + prevAccountBalance
                    + " is not enough");
        }
        accountService.save(account);
        return save(atmBalance);
    }

    @Override
    @Transactional
    public AtmBalance puyMoneyIntoAccount(AtmBalance atmBalance, final Account account, BigDecimal value) {
        account.setBalance(account.getBalance().add(value));
        atmBalance.setBalance(atmBalance.getBalance().add(value));
        accountService.save(account);
        return save(atmBalance);
    }
}
