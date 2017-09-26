package com.oms.customer.model.response;

import com.oms.customer.model.domain.CustomerDomain;

import java.util.List;

public class CustomerResponse {

    private List<CustomerDomain> customer;

    public List<CustomerDomain> getCustomer() {
        return customer;
    }

    public CustomerResponse setCustomer(List<CustomerDomain> customer) {
        this.customer = customer;
        return this;
    }
}
