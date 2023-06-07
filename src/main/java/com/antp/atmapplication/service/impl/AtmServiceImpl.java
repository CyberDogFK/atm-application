package com.antp.atmapplication.service.impl;

import com.antp.atmapplication.model.Atm;
import com.antp.atmapplication.repository.AtmRepository;
import com.antp.atmapplication.service.AtmService;
import java.util.NoSuchElementException;
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

    @Override
    public Atm getById(Long id) {
        return atmRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Can't find atm with id " + id));
    }

    @Override
    public Atm save(Atm atm) {
        return atmRepository.save(atm);
    }
}
