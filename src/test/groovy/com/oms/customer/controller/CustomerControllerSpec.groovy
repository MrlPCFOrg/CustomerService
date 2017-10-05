package com.oms.customer.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.oms.customer.model.response.CustomerResponse
import com.oms.customer.service.CustomerService
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Unroll
import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.FOUND
import static org.springframework.http.HttpStatus.OK
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup
import spock.lang.Specification

class CustomerControllerSpec extends Specification {

    CustomerController subject
    CustomerService customerService
    MockMvc mockMvc

    def setup() {
        customerService = Mock CustomerService
        subject = new CustomerController(customerService)
        mockMvc = standaloneSetup(subject).build()
    }

    def 'addCustomer delegates to service'(){
        given:
        CustomerResponse mockCustomerResponse = new CustomerResponse()

        def billingAddressRequest = [[address :'address', city:'city', state:'state', country:'country', phoneNo:'phoneNo']]
        def customerRequest = [customer:[[name: 'xyx', type:'ADMIN',  billingAddress: billingAddressRequest, email:'email@xyz.com', phoneNo: 'phoneNo']]]
        String requestBody = new ObjectMapper().writeValueAsString(customerRequest)

        when:
        def response = mockMvc.perform(post("/Customer/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)).andReturn().response

        then:
        1 * customerService.addCustomer({
            it
            it.customer[0].name =='xyz'
            it.customer[0].email == 'email@xyz.com'
        }) >> mockCustomerResponse
        response.status == CREATED.value()
    }

    @Unroll
    def 'getCustomerByName delegates to service'() {
        given:
        CustomerResponse mockCustomerResponse = new CustomerResponse()

        when:
        def response = mockMvc.perform(get("/Customer/find/{name}", name)
                .param("isLike", isLike))
                .andReturn().response

        then:
        1 * customerService.getCustomerByName(name, sisLike) >> mockCustomerResponse
        response.status == FOUND.value()

        where:
        name      | isLike  | sisLike
        'Pan'     | 'true'  | true
        'Panneer' | 'false' | false
    }

    def 'deleteCustomer delegates to service'(){
        given:
        String id = "123456"

        when:
        def response = mockMvc.perform(delete("/Customer/delete/{id}", id)).andReturn().response

        then:
        1 * customerService.deleteCustomer(id)
        response.status == OK.value()
    }

    def 'getAllCustomer delegates to service'() {
        given:
        CustomerResponse mockCustomerResponse = new CustomerResponse()

        when:
        def response = mockMvc.perform(get("/Customer/findAll")).andReturn().response

        then:
        1 * customerService.getAllCustomer() >> mockCustomerResponse
        response.status == FOUND.value()
    }

}
