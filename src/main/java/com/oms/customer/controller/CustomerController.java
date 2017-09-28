package com.oms.customer.controller;

import com.oms.customer.model.request.CustomerRequest;
import com.oms.customer.model.response.CustomerResponse;
import com.oms.customer.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse addCustomer(@RequestBody CustomerRequest customerRequest) {
        return customerService.addCustomer(customerRequest);
    }

    @GetMapping("/find/{name}")
    @ResponseStatus(HttpStatus.FOUND)
    public CustomerResponse getCustomer(@PathVariable String name, @RequestParam(required = false) boolean isLike) {
        return customerService.getCustomerByName(name, isLike);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable String id){
        customerService.deleteCustomer(id);
    }

}
