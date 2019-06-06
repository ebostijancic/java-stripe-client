package com.github.ebostijancic.stripe;

public class StripeUtil {


    // converts the given amount in EUR into Eurocent
    public static int getChargeInCents(final Float amount) {
        final Float cents = amount * 100;
        return cents.intValue();
    }
}
