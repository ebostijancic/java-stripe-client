package com.github.ebostijancic.stripe;

import org.apache.commons.validator.routines.EmailValidator;

public class StripeUtil {
    private static final Float MINIMAL_AMOUNT = 0.5f;

    // converts the given amount in EUR into Eurocent
    public static int getChargeInCents(final Float amount) {
        final Float cents = amount * 100;
        return cents.intValue();
    }

    public static boolean isValidAmount(final Float amount) {
        return amount != null && amount.floatValue() < MINIMAL_AMOUNT;
    }

    public static boolean isValidEmail(final String email) {
        return EmailValidator.getInstance().isValid(email);
    }
}
