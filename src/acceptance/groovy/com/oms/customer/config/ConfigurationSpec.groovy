package com.oms.customer.config

class ConfigurationSpec {

    String host

    ConfigurationSpec() {
        if (null == host) {
            host = 'http://localhost:9091'
            println 'INFO: target-test-host: ' + host
        }
    }
}
