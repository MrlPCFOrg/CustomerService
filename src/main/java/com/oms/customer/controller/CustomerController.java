package com.oms.customer.controller;

import com.oms.customer.model.domain.CustomerDomain;
import com.oms.customer.model.request.CustomerRequest;
import com.oms.customer.model.response.CustomerResponse;
import com.oms.customer.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Customer")
public class CustomerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse addCustomer(@RequestBody CustomerRequest customerRequest) {
        LOGGER.info("message=Adding Customer with CustomerRequest={}", customerRequest);
        return customerService.addCustomer(customerRequest);
    }

    @GetMapping("/find/{name}")
    @ResponseStatus(HttpStatus.FOUND)
    public CustomerResponse getCustomer(@PathVariable String name, @RequestParam(required = false) boolean isLike) {
        LOGGER.info("message=Getting Customer with name={}", name);
        return customerService.getCustomerByName(name, isLike);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable String id) {
        LOGGER.info("message=Deleting Customer with id={}", id);
        customerService.deleteCustomer(id);
    }

    @PatchMapping("/update/{customerId}")
    public CustomerResponse updateCustomer(@PathVariable String customerId, @RequestBody CustomerDomain customerUpdate) {
        LOGGER.info("message=Updating Customer with id={}", customerId);
        return customerService.updateCustomer(customerId, customerUpdate);
    }

    @GetMapping("/findAll")
    @ResponseStatus(HttpStatus.FOUND)
    public CustomerResponse getAllCustomers(){
        LOGGER.info("message=Finding All Customers");
        return customerService.getAllCustomer();
    }

}
