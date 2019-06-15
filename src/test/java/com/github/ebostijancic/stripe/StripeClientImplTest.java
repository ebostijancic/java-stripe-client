package com.github.ebostijancic.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Refund;
import com.stripe.model.Source;
import org.junit.Test;
import org.mockito.Mockito;

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

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToCreateChargeWithInvalidCustomer() throws StripeException {
        client.chargeAmount(0.6f, new Customer(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToCreateChargeWithValidSource() throws StripeException {
        final Customer customer = Mockito.mock(Customer.class);
        Mockito.when(customer.getId()).thenReturn("some_valid_id");

        client.chargeAmount(0.6f, customer, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToCaptureAnInvalidCharge() throws StripeException {
        final Charge charge = Mockito.mock(Charge.class);
        Mockito.when(charge.getId()).thenReturn("");

        client.captureCharge(charge);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToCaptureANullCharge() throws StripeException {
        client.captureCharge(null);
    }


    @Test
    public void shouldSucceedToChargeValidAmount() throws StripeException {
        Customer customer = client.addCustomer("emil.bostijancic@gmail.com");

        Source source = client.attachCreditCardSource(customer);

        assertNotNull(client.chargeAmount(100.0f, customer, source));
    }

    @Test
    public void shouldCaptureAValidCharge() throws StripeException {
        final Customer customer = client.addCustomer("emil.bostijancic@gmail.com");

        final Source source = client.attachCreditCardSource(customer);

        final Charge charge = client.chargeAmount(100.0f, customer, source);
        System.out.println(charge.getCaptured());

        assertFalse(charge.getCaptured());

        final Charge captured = client.captureCharge(charge);
        assertTrue(captured.getCaptured());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToCaptureAlreadyCapturedCharge() throws StripeException {
        final Customer customer = client.addCustomer("emil.bostijancic@gmail.com");

        final Source source = client.attachCreditCardSource(customer);

        final Charge charge = client.chargeAmount(100.0f, customer, source);
        System.out.println(charge.getCaptured());

        assertFalse(charge.getCaptured());
        final Charge captured = client.captureCharge(charge);

        client.captureCharge(captured);
    }

    @Test
    public void shouldRefundCharge() throws StripeException {
        final Customer customer = client.addCustomer("emil.bostijancic@gmail.com");

        final Source source = client.attachCreditCardSource(customer);

        final Charge charge = client.chargeAmount(100.0f, customer, source);
        System.out.println(charge.getCaptured());

        Refund refund = client.refundCharge(charge);

        assertNotNull(refund);
        assertEquals("succeeded", refund.getStatus());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToRefundInvalidCharge() throws StripeException {
        final Customer customer = client.addCustomer("emil.bostijancic@gmail.com");

        final Source source = client.attachCreditCardSource(customer);

        final Charge charge = new Charge();
        client.refundCharge(charge);
    }

}

