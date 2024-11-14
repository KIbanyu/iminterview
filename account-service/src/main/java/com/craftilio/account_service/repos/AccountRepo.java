package com.craftilio.account_service.repos;

import com.craftilio.account_service.dto.Accounts;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface AccountRepo extends JpaRepository<Accounts, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Accounts a WHERE a.accountNumber = :accountNumber")
    Accounts findAccountForUpdate(@Param("accountNumber") String accountNumber);

}
