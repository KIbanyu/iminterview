package com.craftilio.customer_service.dtos;

import com.craftilio.customer_service.enums.EntityStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.*;

@Entity
@Data
@Table(name = "customers")
@NoArgsConstructor
public class CustomerEntity{

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "EMAIL")
    private String email;


    @Column(name = "GENDER")
    private String gender;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "ID_NUMBER")
    private String idNumber;


    @Column(name = "STATUS", columnDefinition = "VARCHAR(100)")
    @Enumerated(EnumType.STRING)
    private EntityStatus status;

    @JsonIgnore
    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "PHYSICAL_ADDRESS")
    private String address;

    @Column(name = "COUNTY")
    private String county;

    @Column(name = "CREATED_ON")
    private Date createdOn = new Date();

    @Column(name = "MODIFIED_ON")
    private Date modifiedOn;



    public CustomerEntity(String firstName, String lastName, String email, String gender, String phoneNumber,
                          String idNumber, EntityStatus status, String password, String address, String county) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.idNumber = idNumber;
        this.status = status;
        this.password = password;
        this.address = address;
        this.county = county;
    }



}
