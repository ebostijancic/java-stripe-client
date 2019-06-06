package com.github.ebostijancic.stripe;

import com.stripe.model.Customer;
import com.stripe.model.Source;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class StripeClientImplTest {

    private static final StripeClient client = new StripeClientImpl("sk_test_4eC39HqLyjWDarjtT1zdp7dc");

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWithoutApiKey() {
        new StripeClientImpl(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWithEmptyApiKey() {
        new StripeClientImpl("");
    }

    @Test
    public void shouldSucceedWithValidApiKey() {
        new StripeClientImpl("sk_test_4eC39HqLyjWDarjtT1zdp7dc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToCreateCustomerWithEmptyEmail() {
        client.addCustomer("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToCreateCustomerWithoutEmail() {
        client.addCustomer(null);
    }

    @Test
    public void shouldSucceedToCreateCustomerWithEmail() {
        final String customerEmail = "emil.bostijancic@gmail.com";

        Optional<Customer> customer = client.addCustomer(customerEmail);

        assertTrue(customer.isPresent());
        assertNotNull(customer.get());
        assertEquals(customerEmail, customer.get().getEmail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToAttachCreditCardSourceWithoutCustomer() {
        client.attachCreditCardSource(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToAttachCreditCardSourceWithoutCustomerEmail() {
        client.attachCreditCardSource(new Customer());
    }

    @Test
    public void shouldCreateCreditCardSourceWithValidCustomer() {
        Optional<Customer> customer = client.addCustomer("emil.bostijancic@gmail.com");

        Optional<Source> source = client.attachCreditCardSource(customer.get());

        assertTrue(source.isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToCreateChargeForEmptyAmount() {
        client.chargeAmount(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToCreateChargeForZeroAmount() {
        client.chargeAmount(0.0f, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWithLowerChargenThanMinimalChargeAmount() {
        client.chargeAmount(0.4999f, null, null);
    }

    @Test
    public void shouldSucceedToChargeValidAmount() {
        Optional<Customer> customer = client.addCustomer("emil.bostijancic@gmail.com");

        Optional<Source> source = client.attachCreditCardSource(customer.get());

        assertTrue(client.chargeAmount(100.0f, customer.get(), source.get()));

    }

}

