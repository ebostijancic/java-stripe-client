package com.github.ebostijancic.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Source;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

import static org.junit.Assert.*;

public class WizardTest {
    private StripeClient client = Mockito.mock(StripeClient.class);
    private Customer validCustomer;
    private Source validSource;
    private static final String VALID_EMAIL = "emil.bostijancic@gmail.com";

    private final Wizard wizard = new Wizard(client);


    @Before
    public void setUp() throws StripeException {
        validCustomer = new Customer();
        validCustomer.setEmail(VALID_EMAIL);
        Mockito.when(client.addCustomer(VALID_EMAIL)).thenReturn(validCustomer);

        validSource = Mockito.mock(Source.class);
    }


    @Test
    public void shouldAddCustomer() {
        final InputStream in = new ByteArrayInputStream((VALID_EMAIL + "\r\n").getBytes());
        System.setIn(in);

        final Customer customer = wizard.addCustomer();
        Assert.assertNotNull(customer);
        Assert.assertEquals(validCustomer, customer);
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldFailToAddCustomerWithInvalidEmailAddress() {
        final InputStream in = new ByteArrayInputStream("invalid_email\r\n".getBytes());
        System.setIn(in);

        wizard.addCustomer();
    }

    @Test
    public void shoudlChargeCustomer() throws StripeException {
        final InputStream in = new ByteArrayInputStream("0.7".getBytes());
        System.setIn(in);

        Mockito.when(client.attachCreditCardSource(validCustomer)).thenReturn(validSource);

        final Charge validCharge = Mockito.mock(Charge.class);
        Mockito.when(client.chargeAmount(new Float(0.7f), validCustomer, validSource)).thenReturn(validCharge);

        final Charge charge = wizard.chargeCustomer(validCustomer);

        assertEquals(validCharge, charge);
    }

    @Test
    public void shouldFailToChargeValidCustomer() throws StripeException {
        final InputStream in = new ByteArrayInputStream("0.7".getBytes());
        System.setIn(in);

        Mockito.when(client.attachCreditCardSource(validCustomer)).thenReturn(validSource);
        Mockito.when(client.chargeAmount(0.7f, validCustomer, validSource)).thenThrow(MockStripeException.class);

        assertNull(wizard.chargeCustomer(validCustomer));
    }



    @Test
    public void refundOrCapture() {

    }

    public static class MockStripeException extends StripeException {
        public MockStripeException() {
            this("error", "id", "100", 400);
        }

        public MockStripeException(String message, String requestId, String code, Integer statusCode) {
            super(message, requestId, code, statusCode);
        }
    }

}