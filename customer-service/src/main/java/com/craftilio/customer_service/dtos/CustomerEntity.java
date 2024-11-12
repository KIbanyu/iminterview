package com.craftilio.customer_service.dtos;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "customers")
@NoArgsConstructor
public class CustomerEntity {

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

    @Column(name = "STATUS")
    private String status;

    @Column(name = "PIN")
    private String pin;

    @Column(name = "PHYSICAL_ADDRESS")
    private String address;

    @Column(name = "COUNTY")
    private String county;

    @Column(name = "CREATED_ON")
    private Date createdOn = new Date();

    @Column(name = "MODIFIED_ON")
    private Date modifiedOn;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "MODIFIED_BY")
    private String modifiedBy;

    public CustomerEntity(String firstName, String lastName, String email, String gender, String phoneNumber,
                          String idNumber, String status, String pin, String address, String county,
                          String createdBy, String modifiedBy) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.idNumber = idNumber;
        this.status = status;
        this.pin = pin;
        this.address = address;
        this.county = county;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
    }



}