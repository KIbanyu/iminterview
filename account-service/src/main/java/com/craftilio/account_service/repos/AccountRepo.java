package com.craftilio.account_service.repos;

import com.craftilio.account_service.dto.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepo extends JpaRepository<Accounts, UUID> {
    Optional<Accounts> findByAccountNumber(String accountNumber);
    Accounts getByAccountNumber(String accountNumber);
}
