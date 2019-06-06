package com.github.ebostijancic.stripe;

import com.stripe.model.Customer;
import com.stripe.model.Source;

import java.util.Optional;

public interface StripeClient {

    Optional<Customer> addCustomer(final String email) throws IllegalArgumentException;

    Optional<Source> attachCreditCardSource(final Customer customer) throws IllegalArgumentException;

    boolean chargeAmount(final Float amount, final Customer customer, final Source source) throws IllegalArgumentException;
}
