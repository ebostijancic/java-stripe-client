package com.github.ebostijancic.stripe;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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
}
