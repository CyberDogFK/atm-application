package com.antp.atmapplication.repository;

import com.antp.atmapplication.model.Atm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AtmRepository extends JpaRepository<Atm, Long> {
}
