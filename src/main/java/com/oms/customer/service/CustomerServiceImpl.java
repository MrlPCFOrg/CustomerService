package com.oms.customer.service;

import com.google.common.collect.Lists;
import com.oms.customer.exceptionhandler.BillingAddressException;
import com.oms.customer.exceptionhandler.CustomerNotFoundException;
import com.oms.customer.model.BillingAddress;
import com.oms.customer.model.domain.CustomerDomain;
import com.oms.customer.model.entity.CustomerEntity;
import com.oms.customer.model.request.CustomerRequest;
import com.oms.customer.model.response.CustomerResponse;
import com.oms.customer.repository.CustomerRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRespository;

    private String testConfig;

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
        List<BillingAddress> billingAddressList = customerDomain.getBillingAddress();
        CustomerEntity customerEntity = new CustomerEntity()
                .setName(customerDomain.getName())
                .setType(customerDomain.getType())
                .setCreatedDate(new Date())
                .setEmail(customerDomain.getEmail())
                .setPhoneNo(customerDomain.getPhoneNo());
        billingAddressList.forEach(billingAddress -> {
            billingAddress.setId(UUID.randomUUID().toString());
        });
        customerEntity.setBillingAddress(billingAddressList);
        return customerEntity;

    }

    private CustomerDomain entityToDomain(CustomerEntity customerEntity) {
        return new CustomerDomain()
                .setId(customerEntity.getId())
                .setName(customerEntity.getName())
                .setType(customerEntity.getType())
                .setCreatedDate(customerEntity.getCreatedDate() != null ? customerEntity.getCreatedDate().toInstant() : null)
                .setEmail(customerEntity.getEmail())
                .setPhoneNo(customerEntity.getPhoneNo())
                .setBillingAddress(customerEntity.getBillingAddress());
    }

    @Override
    public void deleteCustomer(String id) {
        customerRespository.delete(id);
    }

    @Override
    public CustomerResponse getCustomerByName(String name, Boolean isLike) {
        CustomerResponse customerResponse = new CustomerResponse();
        List<CustomerEntity> customerRepoList;
        if (isLike) {
            customerRepoList = customerRespository.findByNameLike(name);
        } else {
            customerRepoList = customerRespository.findByName(name);
        }
        List<CustomerDomain> customerResponseList = new ArrayList<>();
        customerRepoList.forEach(customerEntity -> {
            customerResponseList.add(entityToDomain(customerEntity));
        });
        return customerResponse.setCustomer(customerResponseList);
    }

    @Override
    public CustomerResponse updateCustomer(String customerId, CustomerDomain customerUpdate) {
        CustomerEntity customerSource = customerRespository.findById(customerId);
        if (customerSource == null) {
            throw new CustomerNotFoundException();
        }
        CustomerEntity customerToUpdate = sourceCompare(customerUpdate, customerSource);
        CustomerEntity customerUpdated = customerRespository.save(customerToUpdate);
        CustomerResponse customerResponse = new CustomerResponse();
        List<CustomerDomain> customerResponseList = new ArrayList<>();
        customerResponseList.add(entityToDomain(customerUpdated));
        customerResponse.setCustomer(customerResponseList);
        return customerResponse;
    }

    private CustomerEntity sourceCompare(CustomerDomain customerUpdate, CustomerEntity customerSource) {
        CustomerEntity entityUpdate = new CustomerEntity();
        if (customerUpdate == null) {
            throw new CustomerNotFoundException();
        }
        entityUpdate.setId(customerSource.getId());
        entityUpdate.setName(StringUtils.equals(customerUpdate.getName(), customerSource.getName()) ? null : customerUpdate.getName());
        entityUpdate.setType(StringUtils.equals(customerUpdate.getType(), customerSource.getType()) ? null : customerUpdate.getType());
        entityUpdate.setUpdatedDate(new Date());
        entityUpdate.setPhoneNo(StringUtils.equals(customerUpdate.getPhoneNo(), customerSource.getPhoneNo()) ? null : customerUpdate.getPhoneNo());
        entityUpdate.setEmail(StringUtils.equals(customerUpdate.getEmail(), customerSource.getEmail()) ? null : customerUpdate.getEmail());
        entityUpdate.setBillingAddress(billingAddressCompare(customerUpdate.getBillingAddress(), customerSource.getBillingAddress()));
        return entityUpdate;

    }

    private List<BillingAddress> billingAddressCompare(List<BillingAddress> domainAddressList, List<BillingAddress> sourceAddressList) {
        List<BillingAddress> billingAddressListUpdated = new ArrayList<>();
        domainAddressList.forEach(domainAddress -> {
            sourceAddressList.stream().forEach(sourceAddress -> {
                BillingAddress addressUpdate = new BillingAddress();
                if (domainAddress.getId() == null || !isBillingAddressExist(domainAddress.getId(), sourceAddressList)) {
                    throw new BillingAddressException(domainAddress.getId());
                }
                if (StringUtils.equals(domainAddress.getId(), sourceAddress.getId())) {
                    addressUpdate.setId(sourceAddress.getId());
                    addressUpdate.setAddress(domainAddress.getAddress());
                    addressUpdate.setCity(domainAddress.getCity());
                    addressUpdate.setCountry(domainAddress.getCountry());
                    addressUpdate.setState(domainAddress.getState());
                    addressUpdate.setPhoneNo(domainAddress.getPhoneNo());
                }
                billingAddressListUpdated.add(addressUpdate);
            });
        });
        return billingAddressListUpdated;
    }

    private boolean isBillingAddressExist(String id, List<BillingAddress> sourceAddressList) {
        return sourceAddressList.stream().anyMatch(sourceAddress -> sourceAddress.getId().equals(id));
    }


}
