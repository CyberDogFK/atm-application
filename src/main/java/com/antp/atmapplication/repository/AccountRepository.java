package com.antp.atmapplication.repository;

import com.antp.atmapplication.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
