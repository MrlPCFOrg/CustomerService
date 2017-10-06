package com.oms.customer.repository;

import com.oms.customer.model.domain.CustomerDomain;
import com.oms.customer.model.entity.CustomerEntity;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class CustomerRepositoryImpl implements CustomerRepositoryCustom {

    private static final String CUSTOMER_ID = "_id";
    private static final String BILLING_ID = "_id";

    private MongoTemplate mongoTemplate;

    public CustomerRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public CustomerEntity updateCustomer(String customerId, CustomerEntity customerUpdate) {
        List<Pair<Query, Update>> updateOperations = new ArrayList<>();

        List<Pair<Query, Update>> updateOperationsForCustomer = buildCustomerFieldUpdateOperations(customerId,customerUpdate);
        List<Pair<Query, Update>> updateOperationsForBillingAddress = buildBillingAddressUpdateOperations(customerId,customerUpdate);

        if(updateOperationsForCustomer != null)
        updateOperations.addAll(updateOperationsForCustomer);
        updateOperations.addAll(updateOperationsForBillingAddress);

        if (!updateOperations.isEmpty()) {
            BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, CustomerEntity.class);
            bulkOperations.updateMulti(updateOperations).execute();
        }
        Query query = new Query();
        query.addCriteria(where(CUSTOMER_ID).is(new ObjectId(customerId)));
        CustomerEntity customerEntity = mongoTemplate.findOne(query, CustomerEntity.class);

        return customerEntity;
    }

    private List<Pair<Query, Update>> buildBillingAddressUpdateOperations(String customerId, CustomerEntity customerUpdate) {
        if (CollectionUtils.isEmpty(customerUpdate.getBillingAddress())) {
            return Collections.emptyList();
        }
        List<Pair<Query, Update>> billingUpdateOperations = new ArrayList<>();
        customerUpdate.getBillingAddress().stream().forEach(billingAddress -> {
            Query query = new Query();
            query.addCriteria(new Criteria().andOperator(where("_id").is(new ObjectId(customerId)),
                                                         where("billingAddress._id").is(billingAddress.getId())));
            Update update = new Update();

            if(billingAddress.getAddress() != null){
                update.set("billingAddress.$.address", billingAddress.getAddress());
            }
            if(billingAddress.getCity() != null){
                update.set("billingAddress.$.city", billingAddress.getCity());
            }
            if(billingAddress.getCountry() != null) {
                update.set("billingAddress.$.country", billingAddress.getCountry());
            }
            if(billingAddress.getPhoneNo() != null){
                update.set("billingAddress.$.phoneNo",billingAddress.getPhoneNo());
            }
            if(billingAddress.getState() != null) {
                update.set("billingAddress.$.state", billingAddress.getState());
            }
            billingUpdateOperations.add(Pair.of(query,update));
        });

        return billingUpdateOperations;
    }

    private List<Pair<Query, Update>> buildCustomerFieldUpdateOperations(String customerId , CustomerEntity customerUpdate){
        Update update = new Update();
        addFieldIfNotEmpty(update,"name", customerUpdate.getName());
        addFieldIfNotEmpty(update, "type", customerUpdate.getType());
        addFieldIfNotEmpty(update, "phoneNo", customerUpdate.getPhoneNo());
        addFieldIfNotEmpty(update,"email", customerUpdate.getEmail());
        Query query = new Query();
        query.addCriteria(where(CUSTOMER_ID).is(new ObjectId(customerId)));
        if(update.getUpdateObject().keySet().isEmpty()){
            return null;
        }
        return Collections.singletonList(Pair.of(query,update));
  }

    private void addFieldIfNotEmpty(Update update, String fieldName, String updateValue) {
        if (StringUtils.isNotBlank(updateValue)) {
            update.set(fieldName, updateValue);
        }
    }

}
