package com.antp.atmapplication.service.impl;

import com.antp.atmapplication.exception.DataProcessingException;
import com.antp.atmapplication.model.AtmBalance;
import com.antp.atmapplication.repository.AtmBalanceRepository;
import com.antp.atmapplication.service.AtmBalanceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AtmBalanceServiceImpl implements AtmBalanceService {
    private final AtmBalanceRepository atmBalanceRepository;

    public AtmBalanceServiceImpl(AtmBalanceRepository atmBalanceRepository) {
        this.atmBalanceRepository = atmBalanceRepository;
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
    public AtmBalance findById(Long id) {
        return atmBalanceRepository.findById(id).orElseThrow(() ->
                new DataProcessingException("Can't find atm balance with id " + id));
    }

    @Override
    public AtmBalance save(AtmBalance atmBalance) {
        return atmBalanceRepository.save(atmBalance);
    }
}
