package com.oms.customer.exceptionhandler;

public class BillingAddressException extends RuntimeException {

    public BillingAddressException(String BillingAddressId) {
        super("Billing Address id not found: " + BillingAddressId);
    }
}
