package com.oms.customer.model.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oms.customer.model.BillingAddress;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CustomerDomain {

    private String id;
    private String name;
    private String type;
    private Instant createdDate;
    private List<BillingAddress> billingAddress;
    private String email;
    private String phoneNo;
    private Instant updatedDate;

    public String getId() {
        return id;
    }

    public CustomerDomain setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CustomerDomain setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public CustomerDomain setType(String type) {
        this.type = type;
        return this;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public CustomerDomain setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public List<BillingAddress> getBillingAddress() {
        return billingAddress;
    }

    public CustomerDomain setBillingAddress(List<BillingAddress> billingAddress) {
        this.billingAddress = billingAddress;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public CustomerDomain setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public CustomerDomain setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
        return this;
    }

    public Instant getUpdatedDate() {
        return updatedDate;
    }

    public CustomerDomain setUpdatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }
}
