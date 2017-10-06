package com.oms.customer.service

import com.oms.customer.model.BillingAddress
import com.oms.customer.model.domain.CustomerDomain
import com.oms.customer.model.entity.CustomerEntity
import com.oms.customer.model.request.CustomerRequest
import com.oms.customer.model.response.CustomerResponse
import com.oms.customer.repository.CustomerRepository
import org.springframework.test.util.ReflectionTestUtils
import spock.lang.Specification

class CustomerServiceSpec extends Specification {

    CustomerService subject
    CustomerRepository customerRepository

    def setup() {
        customerRepository = Mock CustomerRepository
        subject = new CustomerServiceImpl(customerRepository)
    }

    def 'addCustomer'() {
        given:

        BillingAddress billingAddress = new BillingAddress(address: 'Taramani', city: 'Chennai', country: 'India', phoneNo: '987654321')
        List<BillingAddress> billingAddressReq = new ArrayList<>()
        billingAddressReq.add(billingAddress)
        def customerDomainList = [new CustomerDomain(name: 'Panneer', type: 'ADMIN', email: 'panneerselvam@xyz.com', phoneNo: '12345678', billingAddress: billingAddressReq)]
        CustomerRequest customerReq = new CustomerRequest(customer: customerDomainList)
        def customerEntityList = [new CustomerEntity(id: '123456', name: 'Panneer', type: 'ADMIN', billingAddress: billingAddressReq, email: 'panneerselvam@xyz.com', phoneNo: '12345678')]

        when:
        CustomerResponse customerResponse = subject.addCustomer(customerReq)

        then:
        1 * customerRepository.insert({
            it[0].name == 'Panneer'
        }) >> customerEntityList
        customerResponse
        customerResponse.customer[0].name == 'Panneer'
        customerResponse.customer[0].id == '123456'
        customerResponse.customer[0].type == 'ADMIN'
        customerResponse.customer[0].email == 'panneerselvam@xyz.com'
        customerResponse.customer[0].billingAddress[0].address == 'Taramani'
    }

    def 'searchCustomer'() {
        given:
        String name = 'Panneer'
        boolean isLike = true
        BillingAddress billingAddress = new BillingAddress(address: 'Taramani', city: 'Chennai', country: 'India', phoneNo: '987654321')
        List<BillingAddress> billingAddressReq = new ArrayList<>()
        billingAddressReq.add(billingAddress)
        def customerEntityList = [new CustomerEntity(id: '123456', name: 'Panneer', type: 'ADMIN', billingAddress: billingAddressReq, email: 'panneerselvam@xyz.com', phoneNo: '12345678')]

        when:
        CustomerResponse customerResponse = subject.getCustomerByName(name, isLike)

        then:
        1 * customerRepository.findByNameIsLike({
            it}) >> customerEntityList
        customerResponse
        customerResponse.customer[0].name == 'Panneer'
        customerResponse.customer[0].id == '123456'
        customerResponse.customer[0].type == 'ADMIN'
        customerResponse.customer[0].email == 'panneerselvam@xyz.com'
        customerResponse.customer[0].billingAddress[0].address == 'Taramani'
    }

    def 'deleteCustomer'() {

        given:
        String id = "123456"

        when:
        subject.deleteCustomer(id)

        then:
        1 * customerRepository.delete(id)
    }

    def 'getAllCustomer'() {
        given:
        BillingAddress billingAddress = new BillingAddress(address: 'Taramani', city: 'Chennai', country: 'India', phoneNo: '987654321')
        List<BillingAddress> billingAddressReq = new ArrayList<>()
        billingAddressReq.add(billingAddress)
        def customerEntityList = [new CustomerEntity(id: '123456', name: 'Panneer', type: 'ADMIN', billingAddress: billingAddressReq, email: 'panneerselvam@xyz.com', phoneNo: '12345678')]

        when:
        CustomerResponse customerResponse = subject.getAllCustomer()

        then:
        1 * customerRepository.findAll() >> customerEntityList
        customerResponse
        customerResponse.customer[0].name == 'Panneer'
        customerResponse.customer[0].id == '123456'
        customerResponse.customer[0].type == 'ADMIN'
        customerResponse.customer[0].email == 'panneerselvam@xyz.com'
        customerResponse.customer[0].billingAddress[0].address == 'Taramani'
    }

    def 'updateCustomer'(){
        given:
        BillingAddress billingAddress = new BillingAddress(id:'123456', address: 'Taramani', city: 'Chennai', country: 'India', phoneNo: '987654321')
        List<BillingAddress> billingAddressReq = new ArrayList<>()
        billingAddressReq.add(billingAddress)
        CustomerDomain customerDomain = new CustomerDomain(name: 'Panneer', type: 'ADMIN', email: 'panneerselvam@xyz.com', phoneNo: '12345678', billingAddress: billingAddressReq)
        CustomerEntity customerEntity = new CustomerEntity(id: '123456', name: 'Panneer', type: 'ADMIN', billingAddress: billingAddressReq, email: 'panneerselvam@xyz.com', phoneNo: '12345678')
        String customerId = '123456'

        when:
        CustomerResponse customerResponse = subject.updateCustomer(customerId, customerDomain)

        then:

        1 * customerRepository.findById(customerId) >> customerEntity
        1 * customerRepository.updateCustomer(customerId, {
            it
        } ) >> customerEntity
        customerResponse
        customerResponse.customer[0].name == 'Panneer'
        customerResponse.customer[0].id == '123456'
        customerResponse.customer[0].type == 'ADMIN'
        customerResponse.customer[0].email == 'panneerselvam@xyz.com'
        customerResponse.customer[0].billingAddress[0].address == 'Taramani'
    }

}
