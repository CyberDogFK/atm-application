package com.antp.atmapplication.service;

import com.antp.atmapplication.model.Atm;

import java.util.List;

public interface AtmService {
    List<Atm> getAll();
    Atm getById(Long id);

    Atm save(Atm atm);
}
