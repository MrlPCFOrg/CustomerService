package com.oms.customer

import com.oms.customer.config.ConfigurationSpec
import com.oms.customer.config.CustomerHelper
import groovyx.net.http.RESTClient
import org.springframework.http.HttpStatus
import spock.lang.Shared
import spock.lang.Specification

class CustomerCRUDAccSpec extends Specification{

    @Shared
    ConfigurationSpec configurationSpec

    @Shared
    RESTClient restClient

    @Shared
    CustomerHelper customerHelper = new CustomerHelper()

    def setupSpec(){
        configurationSpec = new ConfigurationSpec()
        restClient = new RESTClient(configurationSpec.host)
    }

    def 'CRUD operation for Customer'(){
        given:
        String customerName = 'Customer-' + UUID.randomUUID()
        def billingAddressRequest = [[address :'Taramani', city:'Chennai', state:'TN', country:'India', phoneNo:'123456789']]
        def customerRequest = [customer:[[name: customerName, type:'ADMIN',  billingAddress: billingAddressRequest, email:'email@xyz.com', phoneNo: '987654321']]]

        when:'Create Customer'
        //Once security is implemented, token will be given
        def createResponse = customerHelper.createCustomer(null, customerRequest)

        then:'Checking the response code for Create Customer'
        createResponse
        createResponse.responseBase.h.original.code == HttpStatus.CREATED.value()

        when:
        def getResponse = customerHelper.getCustomer(null, customerName, true)

        then:'Checking the response code for Get Customer'
        getResponse
        getResponse.status == HttpStatus.FOUND.value()
        getResponse.responseData.customer[0].name == customerName

        when:
        def getAllResponse = customerHelper.getAllCustomer(null)

        then:'Checking the response code for Get All Customer'
        getAllResponse
        getAllResponse.status == HttpStatus.FOUND.value()
        getAllResponse.responseData.customer.size > 1

        def customerId = getResponse.responseData.customer[0].id

        cleanup:
        customerHelper.deleteCustomer(null, customerId)

    }


}
