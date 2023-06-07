package com.antp.atmapplication.repository;

import com.antp.atmapplication.model.AtmBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AtmBalanceRepository extends JpaRepository<AtmBalance, Long> {
}
