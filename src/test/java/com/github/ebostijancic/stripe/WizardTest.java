package com.github.ebostijancic.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;

import static org.junit.Assert.*;

public class WizardTest {
    private StripeClient client = Mockito.mock(StripeClient.class);
    private Customer validCustomer;
    private static final String VALID_EMAIL = "emil.bostijancic@gmail.com";

    @Before
    public void setUp() throws StripeException {
        validCustomer = new Customer();
        validCustomer.setEmail(VALID_EMAIL);
        Mockito.when(client.addCustomer(VALID_EMAIL)).thenReturn(validCustomer);
    }


    @Test
    public void shouldAddCustomer() {
        final InputStream in = new ByteArrayInputStream(VALID_EMAIL.getBytes());
        System.setIn(in);

        final Wizard wizard = new Wizard(client);
        final Customer customer = wizard.addCustomer();
        Assert.assertNotNull(customer);
        Assert.assertEquals(validCustomer, customer);
    }

    @Test
    public void chargeCustomer() {

    }

    @Test
    public void refundOrCapture() {

    }
}