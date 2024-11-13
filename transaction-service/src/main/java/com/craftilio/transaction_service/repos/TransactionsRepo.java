package com.craftilio.transaction_service.repos;

import com.craftilio.transaction_service.dtos.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionsRepo extends JpaRepository<TransactionEntity, UUID> {
}
