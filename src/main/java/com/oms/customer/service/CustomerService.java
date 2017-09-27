package com.oms.customer.service;

import com.oms.customer.model.request.CustomerRequest;
import com.oms.customer.model.response.CustomerResponse;

public interface CustomerService {

    CustomerResponse addCustomer(CustomerRequest customerRequest);
}
