package com.oms.customer

import com.oms.customer.config.ConfigurationSpec
import com.oms.customer.config.CustomerHelper
import com.oms.customer.model.BillingAddress
import com.oms.customer.model.domain.CustomerDomain
import groovyx.net.http.RESTClient
import org.springframework.http.HttpStatus
import spock.lang.Shared
import spock.lang.Specification

class CustomerCRUDAccSpec extends Specification {

    @Shared
    ConfigurationSpec configurationSpec

    @Shared
    RESTClient restClient

    @Shared
    CustomerHelper customerHelper = new CustomerHelper()

    def setupSpec() {
        configurationSpec = new ConfigurationSpec()
        restClient = new RESTClient(configurationSpec.host)
    }

    def 'CRUD operation for Customer'() {
        given:
        String customerName = 'Customer-' + UUID.randomUUID()
        def billingAddressRequest = [[address: 'Taramani', city: 'Chennai', state: 'TN', country: 'India', phoneNo: '123456789']]
        def customerRequest = [customer: [[name: customerName, type: 'ADMIN', billingAddress: billingAddressRequest, email: 'email@xyz.com', phoneNo: '987654321']]]

        String custId;
        BillingAddress billingAddress = new BillingAddress(address: 'Taramani', city: 'Chennai', country: 'India', phoneNo: '987654321')
        List<BillingAddress> billingAddressReq = new ArrayList<>()
        billingAddressReq.add(billingAddress)
        def customerDomain = [name: 'Panneer', type: 'ADMIN', email: 'panneerselvam@xyz.com', phoneNo: '12345678', billingAddress: billingAddressReq]

        when: 'Create Customer'
        //Once security is implemented, token will be given
        def createResponse = customerHelper.createCustomer(null, customerRequest)

        then: 'Checking the response code for Create Customer'
        createResponse
        createResponse.responseBase.h.original.code == HttpStatus.CREATED.value()

        when:
        def getResponse = customerHelper.getCustomer(null, customerName, true)

        then: 'Checking the response code for Get Customer'
        getResponse
        getResponse.status == HttpStatus.FOUND.value()
        getResponse.responseData.customer[0].name == customerName

        when:
        custId = createResponse.responseData.customer[0].id
        customerDomain.billingAddress[0].id = createResponse.responseData.customer[0].billingAddress[0].id

        def updateResponse = customerHelper.updateCustomer(null, custId, customerDomain)

        then:'Checking the response for update Customer'
        updateResponse
        updateResponse.responseData.customer[0].name == 'Panneer'
        updateResponse.responseData.customer[0].phoneNo == '12345678'
        updateResponse.responseData.customer[0].billingAddress[0].address == 'Taramani'
        updateResponse.responseData.customer[0].billingAddress[0].city == 'Chennai'

        when:
        def getAllResponse = customerHelper.getAllCustomer(null)

        then: 'Checking the response code for Get All Customer'
        getAllResponse
        getAllResponse.status == HttpStatus.FOUND.value()
        getAllResponse.responseData.customer.size > 1

        def customerId = getResponse.responseData.customer[0].id

        cleanup:
        customerHelper.deleteCustomer(null, customerId)

    }


}
