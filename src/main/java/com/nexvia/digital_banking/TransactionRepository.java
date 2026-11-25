package com.nexvia.digital_banking;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TransactionRepository
        extends JpaRepository<BankTransaction, Long> {

    Optional<BankTransaction> findByIdempotencyKey(String key);

    long countByFromAccount_Id(Long fromAccountId);
}