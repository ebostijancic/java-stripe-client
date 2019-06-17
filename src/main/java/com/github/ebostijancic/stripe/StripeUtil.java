package com.github.ebostijancic.stripe;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.util.StringUtils;

public class StripeUtil {
    private static final Float MINIMAL_AMOUNT = 0.5f;
    private static final String REFUND_SHORT = "r";
    private static final String REFUND_LONG = "refund";

    private static final String CAPTURE_SHORT = "c";
    private static final String CAPTURE_LONG = "capture";

    // converts the given amount in EUR into Eurocent
    public static int getChargeInCents(final float amount) {
        final Float cents = amount * 100;
        return cents.intValue();
    }

    public static boolean isValidAmount(final float amount) {
        return amount >= MINIMAL_AMOUNT;
    }

    public static boolean isValidEmail(final String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    public static boolean isValidAnswer(final String answer) {
        if (StringUtils.isEmpty(answer)) {
            return false;
        }

        final String lowerAnswer = answer.toLowerCase();

        return (CAPTURE_SHORT.equals(lowerAnswer) ||
                CAPTURE_LONG.equals(lowerAnswer) ||
                REFUND_SHORT.equals(lowerAnswer) ||
                REFUND_LONG.equals(lowerAnswer));
    }

    public static boolean isRefund(final String answer) {
        final String lowerAnswer = answer.toLowerCase();
        return isValidAnswer(answer) && (REFUND_SHORT.equals(lowerAnswer) || REFUND_LONG.equals(lowerAnswer));
    }

    public static boolean isCapture(final String answer) {
        final String lowerAnswer = answer.toLowerCase();
        return isValidAnswer(answer) && (CAPTURE_SHORT.equals(lowerAnswer) || CAPTURE_LONG.equals(lowerAnswer));
    }


}
