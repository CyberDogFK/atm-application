package com.antp.atmapplication.service.impl;

import com.antp.atmapplication.exception.DataProcessingException;
import com.antp.atmapplication.model.Currency;
import com.antp.atmapplication.repository.CurrencyRepository;
import com.antp.atmapplication.service.CurrencyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public List<Currency> getAll() {
        return currencyRepository.findAll();
    }

    @Override
    public Currency getById(Long id) {
        return currencyRepository.findById(id).orElseThrow(() ->
                new DataProcessingException("Can't find currency by id: " + id));
    }

    @Override
    public Currency save(Currency currency) {
        return currencyRepository.save(currency);
    }
}
