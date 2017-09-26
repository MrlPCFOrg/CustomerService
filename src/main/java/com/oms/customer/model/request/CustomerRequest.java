package com.oms.customer.model.request;

import com.oms.customer.model.domain.CustomerDomain;
import java.util.List;

public class CustomerRequest {

    private List<CustomerDomain> customer;

    public List<CustomerDomain> getCustomer() {
        return customer;
    }

    public CustomerRequest setCustomer(List<CustomerDomain> customer) {
        this.customer = customer;
        return this;
    }
}
