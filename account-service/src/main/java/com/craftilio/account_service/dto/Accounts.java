package com.craftilio.account_service.dto;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "acconts")
public class Accounts {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "ACCOUNT_NUMBER", unique = true, nullable = false)
    private String accountNumber;

    @Column(name = "ACCOUNT_NAME")
    private String accountName;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "ACCOUNT_TYPE")
    private String accountType;

    @Column(name = "BALANCE")
    private BigDecimal balance;

    @Column(name = "CUSTOMER_ID", nullable = false)
    private UUID customerId;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CREATED_ON")
    private Date createdOn = new Date();

    @Column(name = "MODIFIED_ON")
    private Date modifiedOn;

    @Column(name = "CREATED_BY")
    private String createdBy ;

    @Column(name = "MODIFIED_BY")
    private String modifiedBy;

    @Column(name = "COMMENT")
    private String comment;
}
