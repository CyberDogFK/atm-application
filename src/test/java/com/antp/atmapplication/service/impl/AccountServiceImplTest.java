package com.antp.atmapplication.service.impl;

import com.antp.atmapplication.exception.DataProcessingException;
import com.antp.atmapplication.model.Account;
import com.antp.atmapplication.model.Currency;
import com.antp.atmapplication.model.Role;
import com.antp.atmapplication.model.User;
import com.antp.atmapplication.repository.AccountRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountServiceImpl accountService;


    @Test
    void findUserAccountById_Ok() {
        final var expectedAccountId = 99L;
        final var mockCurrency = new Currency();
        final var user = getMockUser();
        final var expectedAccount = new Account(
                expectedAccountId,
                user,
                mockCurrency,
                BigDecimal.ZERO
        );
        user.setAccounts(
                List.of(
                        expectedAccount,
                        new Account(expectedAccountId, user, mockCurrency, BigDecimal.ZERO),
                        new Account(2L, user, mockCurrency, BigDecimal.ZERO),
                        new Account(3L, user, mockCurrency, BigDecimal.ZERO
                        )
                )
        );
        Account actual = accountService.findUserAccountById(user, expectedAccountId);
        Assertions.assertEquals(expectedAccount, actual,
                "You must return account with specified id from user");
        Assertions.assertEquals(expectedAccountId, actual.getId(),
                "You must find account with specified id from user");
    }

    @Test
    void findUserAccountById_NotOk() {
        final var mockCurrency = new Currency();
        final var user = getMockUser();
        user.setAccounts(
                List.of(
                        new Account(1L, user, mockCurrency, BigDecimal.ZERO),
                        new Account(2L, user, mockCurrency, BigDecimal.ZERO),
                        new Account(3L, user, mockCurrency, BigDecimal.ZERO)
                )
        );
        Assertions.assertThrows(DataProcessingException.class,() ->
                accountService.findUserAccountById(user, 99L),
                "If there no any account with specific id you must throw exception");

    }

    @Test
    void transferMoney_Ok() {
        final var mockUser = getMockUser();
        final var mockCurrency = new Currency();
        final var transferringValue = BigDecimal.valueOf(1000L);
        final var transferFromAccount = new Account(
                1L,
                mockUser,
                mockCurrency,
                new BigDecimal(100)
        );
        final var transferToAccount = new Account(
                2L,
                mockUser,
                mockCurrency,
                new BigDecimal(0)
        );

        accountService.transferMoney(transferFromAccount, transferToAccount, transferringValue);
    }

    private User getMockUser() {
        final var userRole = new Role(
                1L,
                Role.RoleName.USER
        );
        return new User(
                1L,
                "Bob",
                "1234",
                userRole,
                List.of()
        );
    }
}
