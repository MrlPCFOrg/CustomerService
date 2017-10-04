package com.oms.customer.model.entity;

import com.oms.customer.model.BillingAddress;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Document(collection = "customer")
public class CustomerEntity {

    @Id
    private String id;

    private String name;
    private String type;

    @DateTimeFormat
    private Date createdDate;

    private List<BillingAddress> billingAddress;
    private String email;
    private String phoneNo;

    @DateTimeFormat
    private Date updatedDate;


    public String getId() {
        return id;
    }

    public CustomerEntity setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CustomerEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public CustomerEntity setType(String type) {
        this.type = type;
        return this;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public CustomerEntity setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public List<BillingAddress> getBillingAddress() {
        return billingAddress;
    }

    public CustomerEntity setBillingAddress(List<BillingAddress> billingAddress) {
        this.billingAddress = billingAddress;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public CustomerEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public CustomerEntity setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
        return this;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public CustomerEntity setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }

    public <T> void fieldValueIfUpdated(T updateValue, T sourceValue, Function<T, ?> diffSetter) {
        if (updateValue != null && !updateValue.equals(sourceValue)) {
            diffSetter.apply(updateValue);
        }
    }
}
