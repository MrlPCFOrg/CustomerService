package com.oms.customer.config

import groovyx.net.http.RESTClient

class CustomerHelper {

    RESTClient restClient

    CustomerHelper() {
        ConfigurationSpec configurationSpec = new ConfigurationSpec()
        restClient = new RESTClient(configurationSpec.host)
        restClient.handler.failure = { resp, data ->
            resp.setData(data)
            String headers = ''
            resp.headers.each { h ->
                headers = headers + "${h.name} : ${h.value}\n"
            }
            return resp
        }
    }

    def createCustomer(String token, Map customerRequest) {
        def response = restClient.post(
                path: "/Customer/add",
                contentType: 'application/json;charset=UTF-8',
                headers: ['Authorization': "Bearer ${token}", 'Content-Type': 'application/json'],
                body: customerRequest
        )
        return response
    }

    def getCustomer(String token, String name, boolean isLike) {
        def response = restClient.get(
                path: "/Customer/find/${name}",
                headers: ['Authorization': "Bearer ${token}", 'Content-Type': 'application/json'],
                query: ['isLike': isLike])
        return response
    }

    def deleteCustomer(String token, String id) {
        def response = restClient.delete(
                path: "/Customer/delete/${id}",
                headers: ['Authorization': "Bearer ${token}", 'Content-Type': 'application/json'])
        return response
    }

    def getAllCustomer(String token) {
        def response = restClient.get(
                path: "/Customer/findAll",
                headers: ['Authorization': "Bearer ${token}", 'Content-Type': 'application/json'])
        return response
    }
}
