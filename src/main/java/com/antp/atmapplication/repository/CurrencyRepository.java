package com.antp.atmapplication.repository;

import com.antp.atmapplication.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
}
