package com.github.ebostijancic.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Source;
import org.junit.Test;

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
    public void shouldFailToCreateCustomerWithEmptyEmail() throws StripeException {
        client.addCustomer("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToCreateCustomerWithoutEmail() throws StripeException {
        client.addCustomer(null);
    }

    @Test
    public void shouldSucceedToCreateCustomerWithEmail() throws StripeException {
        final String customerEmail = "emil.bostijancic@gmail.com";

        Customer customer = client.addCustomer(customerEmail);

        assertNotNull(customer);
        assertEquals(customerEmail, customer.getEmail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToAttachCreditCardSourceWithoutCustomer() throws StripeException {
        client.attachCreditCardSource(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToAttachCreditCardSourceWithoutCustomerEmail() throws StripeException {
        client.attachCreditCardSource(new Customer());
    }

    @Test
    public void shouldCreateCreditCardSourceWithValidCustomer() throws StripeException {
        Customer customer = client.addCustomer("emil.bostijancic@gmail.com");

        Source source = client.attachCreditCardSource(customer);

        assertTrue(source != null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToCreateChargeForEmptyAmount() throws StripeException {
        client.chargeAmount(0.0f, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToCreateChargeForZeroAmount() throws StripeException {
        client.chargeAmount(0.0f, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWithLowerChargenThanMinimalChargeAmount() throws StripeException {
        client.chargeAmount(0.4999f, null, null);
    }

    @Test
    public void shouldSucceedToChargeValidAmount() throws StripeException {
        Customer customer = client.addCustomer("emil.bostijancic@gmail.com");

        Source source = client.attachCreditCardSource(customer);

        assertNotNull(client.chargeAmount(100.0f, customer, source));
    }


}

