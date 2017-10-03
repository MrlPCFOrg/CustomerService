package com.oms.customer

import com.oms.customer.config.ConfigurationSpec
import groovyx.net.http.RESTClient
import spock.lang.Shared
import spock.lang.Specification

class HealthAccSpec extends Specification{

    @Shared
    ConfigurationSpec configurationSpec

    @Shared
    RESTClient restClient

    def setupSpec(){
        configurationSpec = new ConfigurationSpec()
        restClient = new RESTClient(configurationSpec.host)
        restClient.setHeaders(Accept:'application/json')
    }

    def "/health returns application status"() {

        when:
        def response = restClient.get(path: '/health')
        def responseBody = response.data

        then:
        response.status == 200
        response.headers.'Content-Type'.toString() == 'application/json;charset=UTF-8'
        responseBody.status == 'UP'

    }

}
