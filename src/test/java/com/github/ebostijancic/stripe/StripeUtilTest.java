package com.github.ebostijancic.stripe;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class StripeUtilTest {

    @Test
    public void shouldConvertEurIntoEurocent() {
        assertEquals(100, StripeUtil.getChargeInCents(1.0f));
        assertEquals(50, StripeUtil.getChargeInCents(0.5f));
        assertEquals(49, StripeUtil.getChargeInCents(0.49f));
    }

    @Test
    public void shouldValidateEmail() {
        assertFalse(StripeUtil.isValidEmail("email_invlaid"));
    }

    @Test
    public void shouldReturnValidAnswer() {
        assertTrue(StripeUtil.isValidAnswer("r"));
        assertTrue(StripeUtil.isValidAnswer("R"));
        assertTrue(StripeUtil.isValidAnswer("c"));
        assertTrue(StripeUtil.isValidAnswer("C"));
        assertTrue(StripeUtil.isValidAnswer("refund"));
        assertTrue(StripeUtil.isValidAnswer("capture"));

        assertFalse(StripeUtil.isValidEmail("emil"));
        assertFalse(StripeUtil.isValidEmail("e"));
        assertFalse(StripeUtil.isValidEmail("rc"));
    }

    @Test
    public void shouldReturnIsRefund() {
        assertTrue(StripeUtil.isRefund("r"));
        assertTrue(StripeUtil.isRefund("refund"));

        assertFalse(StripeUtil.isRefund("c"));
        assertFalse(StripeUtil.isRefund("capture"));
        assertFalse(StripeUtil.isRefund("anyother"));
    }

    @Test
    public void shouldReturnIsCapture() {
        assertTrue(StripeUtil.isCapture("c"));
        assertTrue(StripeUtil.isCapture("capture"));

        assertFalse(StripeUtil.isCapture("r"));
        assertFalse(StripeUtil.isCapture("refund"));
        assertFalse(StripeUtil.isCapture("anyother"));
    }

    @Test
    public void shouldValidateAmount() {
        assertFalse(StripeUtil.isValidAmount(0.4f));
        assertTrue(StripeUtil.isValidAmount(0.5f));
        assertTrue(StripeUtil.isValidAmount(1.5f));
        assertTrue(StripeUtil.isValidAmount(100.5f));
    }
}
