package com.craftilio.customer_service.repos;

import com.craftilio.customer_service.dtos.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepo extends JpaRepository<CustomerEntity, UUID> {

    Optional<CustomerEntity> findByPhoneNumber(String phoneNumber);
    Optional<CustomerEntity> findByIdNumber(String idNumber);
    Optional<CustomerEntity> findByEmailAndPassword(String email, String password);


}
