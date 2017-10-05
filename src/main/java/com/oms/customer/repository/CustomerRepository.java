package com.oms.customer.repository;

import com.oms.customer.model.entity.CustomerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CustomerRepository extends MongoRepository<CustomerEntity,String> , CustomerRepositoryCustom{

    List<CustomerEntity> findByName(String name);

    @Query(value = "{'name': {$regex : ?0, $options: 'i'}}")
    List<CustomerEntity> findByNameLike(String name);

    CustomerEntity findById(String customerId);

}
