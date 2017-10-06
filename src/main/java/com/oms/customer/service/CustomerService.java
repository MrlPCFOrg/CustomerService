package com.oms.customer.service;

import com.oms.customer.model.domain.CustomerDomain;
import com.oms.customer.model.request.CustomerRequest;
import com.oms.customer.model.response.CustomerResponse;

public interface CustomerService {

    CustomerResponse addCustomer(CustomerRequest customerRequest);

    CustomerResponse getCustomerByName(String name, boolean isLike);

    void deleteCustomer(String id);

    CustomerResponse updateCustomer(String customerId, CustomerDomain customerUpdate);

    CustomerResponse getAllCustomer();
}
