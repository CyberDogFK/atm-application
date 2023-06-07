package com.antp.atmapplication.service.impl;

import com.antp.atmapplication.exception.DataProcessingException;
import com.antp.atmapplication.model.Account;
import com.antp.atmapplication.repository.AccountRepository;
import com.antp.atmapplication.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id).orElseThrow(() ->
                new DataProcessingException("Can't find account with id " + id));
    }

    @Override
    public List<Account> findAllByIds(List<Long> ids) {
        return accountRepository.findAllById(ids);
    }

    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }
}
