package com.application.elerna.repository;

import com.application.elerna.model.BankAccountLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountLogRepository extends JpaRepository<BankAccountLog, Long> {
}
