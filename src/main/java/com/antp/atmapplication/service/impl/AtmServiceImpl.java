package com.antp.atmapplication.service.impl;

import com.antp.atmapplication.model.Atm;
import com.antp.atmapplication.repository.AtmRepository;
import com.antp.atmapplication.service.AtmService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AtmServiceImpl implements AtmService {
    private final AtmRepository atmRepository;

    public AtmServiceImpl(AtmRepository atmRepository) {
        this.atmRepository = atmRepository;
    }

    @Override
    public List<Atm> getAll() {
        return atmRepository.findAll();
    }
}
