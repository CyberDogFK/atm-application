package com.antp.atmapplication.service.impl;

import com.antp.atmapplication.exception.DataProcessingException;
import com.antp.atmapplication.model.Account;
import com.antp.atmapplication.model.User;
import com.antp.atmapplication.repository.AccountRepository;
import com.antp.atmapplication.service.AccountService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    @Override
    @Transactional
    public Account transferMoney(Account from, Account to, BigDecimal value) {
        if (from.getBalance().compareTo(value) < 0)  {
            throw new DataProcessingException("On from account not enough balance");
        }
        BigDecimal balance = from.getBalance();
        from.setBalance(from.getBalance().subtract(value));
        to.setBalance(to.getBalance().add(value));
        accountRepository.save(to);
        Account saved = accountRepository.save(from);
        if (balance.subtract(value).compareTo(saved.getBalance()) != 0) {
            throw new RuntimeException("Error while processing transfer");
        }
        return saved;
    }

    @Override
    public Account findUserAccountById(User user, Long accountId) {
        return user.getAccounts().stream()
                .filter(it -> it.getId().equals(accountId))
                .findFirst().orElseThrow(() ->
                        new RuntimeException("Can't find user " + user.getName()
                                + " account with accountId " + accountId));
    }
}
