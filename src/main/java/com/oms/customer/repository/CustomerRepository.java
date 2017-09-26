package com.oms.customer.repository;

import com.oms.customer.model.entity.CustomerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository <CustomerEntity,String>{

}
