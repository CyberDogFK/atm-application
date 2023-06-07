package com.antp.atmapplication.service;

import com.antp.atmapplication.model.Account;

import java.util.List;

public interface AccountService {
    List<Account> getAll();

    Account findById(Long id);

    List<Account> findAllByIds(List<Long> id);

    Account save(Account account);
}
