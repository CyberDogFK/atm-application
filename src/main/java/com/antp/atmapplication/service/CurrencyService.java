package com.antp.atmapplication.service;

import com.antp.atmapplication.model.Currency;

import java.util.List;

public interface CurrencyService {
    List<Currency> getAll();
    Currency getById(Long id);
}
