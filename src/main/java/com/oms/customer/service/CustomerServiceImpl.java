package com.oms.customer.service;

import com.oms.customer.exceptionhandler.BillingAddressException;
import com.oms.customer.exceptionhandler.CustomerNotFoundException;
import com.oms.customer.model.BillingAddress;
import com.oms.customer.model.domain.CustomerDomain;
import com.oms.customer.model.entity.CustomerEntity;
import com.oms.customer.model.request.CustomerRequest;
import com.oms.customer.model.response.CustomerResponse;
import com.oms.customer.repository.CustomerRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerResponse addCustomer(CustomerRequest customerRequest) {

        CustomerResponse customerResponse = new CustomerResponse();

        List<CustomerDomain> customerRequestList = customerRequest.getCustomer();
        List<CustomerEntity> customerEntityList = new ArrayList<>();

        customerRequestList.forEach(customerDomain -> {
            customerEntityList.add(domainToEntity(customerDomain));
        });

        List<CustomerEntity> customerRepoList = customerRepository.insert(customerEntityList);
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
        customerRepository.delete(id);
    }

    @Override
    public CustomerResponse getCustomerByName(String name, boolean isLike) {
        if (StringUtils.isEmpty(name)){
            LOGGER.info("message=CustomerName is empty");
            throw new CustomerNotFoundException();
        }
        CustomerResponse customerResponse = new CustomerResponse();
        List<CustomerEntity> customerRepoList;
        if (isLike) {
            customerRepoList = customerRepository.findByNameLike(name);
        } else {
            customerRepoList = customerRepository.findByName(name);
        }
        List<CustomerDomain> customerResponseList = new ArrayList<>();
        customerRepoList.forEach(customerEntity -> {
            customerResponseList.add(entityToDomain(customerEntity));
        });
        return customerResponse.setCustomer(customerResponseList);
    }

    @Override
    public CustomerResponse updateCustomer(String customerId, CustomerDomain customerUpdate) {
        CustomerEntity customerSource = customerRepository.findById(customerId);
        if (customerSource == null) {
            LOGGER.info("message=CustomerId is not found in the repository");
            throw new CustomerNotFoundException();
        }
        CustomerEntity customerToUpdate = sourceCompare(customerUpdate, customerSource);
        CustomerEntity customerUpdated = customerRepository.updateCustomer(customerId,customerToUpdate);
        CustomerResponse customerResponse = new CustomerResponse();
        List<CustomerDomain> customerResponseList = new ArrayList<>();
        customerResponseList.add(entityToDomain(customerUpdated));
        customerResponse.setCustomer(customerResponseList);
        return customerResponse;
    }

    private CustomerEntity sourceCompare(CustomerDomain customerUpdate, CustomerEntity customerSource) {
        CustomerEntity entityUpdate = new CustomerEntity();
        if (customerUpdate == null) {
            LOGGER.info("message=CustomerName is not found in the repository");
            throw new CustomerNotFoundException();
        }
        entityUpdate.setId(customerSource.getId());
        entityUpdate.fieldValueIfUpdated(customerUpdate.getName(),customerSource.getName(), entityUpdate::setName);
        entityUpdate.fieldValueIfUpdated(customerUpdate.getType(), customerSource.getType(), entityUpdate::setType);
        entityUpdate.setUpdatedDate(new Date());
        entityUpdate.fieldValueIfUpdated(customerUpdate.getPhoneNo(), customerSource.getPhoneNo(), entityUpdate::setPhoneNo);
        entityUpdate.fieldValueIfUpdated(customerUpdate.getEmail(), customerSource.getEmail(), entityUpdate::setEmail);
        entityUpdate.setBillingAddress(billingAddressCompare(customerUpdate.getBillingAddress(), customerSource.getBillingAddress()));
        return entityUpdate;

    }

    private List<BillingAddress> billingAddressCompare(List<BillingAddress> domainAddressList, List<BillingAddress> sourceAddressList) {
        if(domainAddressList == null){
            return null;
        }
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

    @Override
    public CustomerResponse getAllCustomer(){
        CustomerResponse customerResponse = new CustomerResponse();
        List<CustomerEntity> customerRepoList;
        List<CustomerDomain> customerResponseList = new ArrayList<>();
        customerRepoList = customerRepository.findAll();
        customerRepoList.forEach(customerEntity -> {
            customerResponseList.add(entityToDomain(customerEntity));
        });
        return customerResponse.setCustomer(customerResponseList);
    }
}
