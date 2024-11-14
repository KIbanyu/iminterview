package com.craftilio.transaction_service.dtos;

import com.craftilio.transaction_service.enums.EntityStatus;
import com.craftilio.transaction_service.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "TRANSACTION_TYPE", columnDefinition = "VARCHAR(100)")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "TO_ACCOUNT")
    private String toAccountId;

    @Column(name = "FROM_ACCOUNT")
    private String fromAccountId;

    @Column(name = "CREATED_ON")
    private Date createdOn = new Date();

    @Column(name = "MODIFIED_ON")
    private Date modifiedOn;

    @Column(name = "STATUS", columnDefinition = "VARCHAR(100)")
    @Enumerated(EnumType.STRING)
    private EntityStatus status;

    @Column(name = "NARRATION")
    private String narration;


}
