package com.oms.customer.service;

import com.oms.customer.model.domain.CustomerDomain;
import com.oms.customer.model.entity.CustomerEntity;
import com.oms.customer.model.request.CustomerRequest;
import com.oms.customer.model.response.CustomerResponse;
import com.oms.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRespository;

    public CustomerServiceImpl(CustomerRepository customerRespository) {
        this.customerRespository = customerRespository;
    }

    @Override
    public CustomerResponse addCustomer(CustomerRequest customerRequest) {

        CustomerResponse customerResponse = new CustomerResponse();

        List<CustomerDomain> customerRequestList = customerRequest.getCustomer();
        List<CustomerEntity> customerEntityList = new ArrayList<>();

        customerRequestList.forEach(customerDomain -> {
            customerEntityList.add(domainToEntity(customerDomain));
        });

        List<CustomerEntity> customerRepoList = customerRespository.insert(customerEntityList);
        List<CustomerDomain> customerResponseList = new ArrayList<>();
        customerRepoList.forEach(customerEntity -> {
            customerResponseList.add(entityToDomain(customerEntity));
        });

        return customerResponse.setCustomer(customerResponseList);
    }

    private CustomerEntity domainToEntity(CustomerDomain customerDomain) {
        return new CustomerEntity()
                .setName(customerDomain.getName())
                .setType(customerDomain.getType())
                .setCreatedDate(new Date())
                .setEmail(customerDomain.getEmail())
                .setPhoneNo(customerDomain.getPhoneNo())
                .setBillingAddress(customerDomain.getBillingAddress());
    }

    private CustomerDomain entityToDomain(CustomerEntity customerEntity){
        return new CustomerDomain()
                .setId(customerEntity.getId())
                .setName(customerEntity.getName())
                .setType(customerEntity.getType())
                .setCreatedDate(customerEntity.getCreatedDate().toInstant())
                .setEmail(customerEntity.getEmail())
                .setPhoneNo(customerEntity.getPhoneNo())
                .setBillingAddress(customerEntity.getBillingAddress());
    }

    @Override
    public void deleteCustomer(String id){
        customerRespository.delete(id);
    }

    @Override
    public CustomerResponse getCustomerByName(String name, Boolean isLike){
        CustomerResponse customerResponse = new CustomerResponse();
        List<CustomerEntity> customerRepoList;
        if(isLike){
            customerRepoList= customerRespository.findByNameLike(name);
        }else{
            customerRepoList = customerRespository.findByName(name);
        }
        List<CustomerDomain> customerResponseList = new ArrayList<>();
        customerRepoList.forEach(customerEntity -> {
            customerResponseList.add(entityToDomain(customerEntity));
        });
        return customerResponse.setCustomer(customerResponseList);

    }

}
