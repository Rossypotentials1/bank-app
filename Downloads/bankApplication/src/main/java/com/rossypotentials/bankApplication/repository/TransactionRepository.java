package com.rossypotentials.bankApplication.repository;

import com.rossypotentials.bankApplication.domain.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transactions,String> {
}
