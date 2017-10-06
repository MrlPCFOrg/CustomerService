package com.oms.customer.repository;

import com.oms.customer.model.entity.CustomerEntity;

public interface CustomerRepositoryCustom {

    CustomerEntity updateCustomer(String customerId, CustomerEntity customerUpdate);
}
