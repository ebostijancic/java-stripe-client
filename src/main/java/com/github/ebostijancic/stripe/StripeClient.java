package com.github.ebostijancic.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Refund;
import com.stripe.model.Source;

public interface StripeClient {

    /**
     * Adds a new customer to the account by it's email.
     *
     * @param email email address of the customer
     * @return optionally a Customer object containing all customer information.
     * @throws IllegalArgumentException in case an invalid email address was given.
     */
    Customer addCustomer(final String email) throws IllegalArgumentException, StripeException;

    /**
     * Attaches a credit card source to a given customer, so that it can be used to
     * charge the customer through this source.
     *
     * @param customer Customer to attach the credit card.
     * @return optionally the create attached credit card source.
     * @throws IllegalArgumentException when customer is invalid.
     */
    Source attachCreditCardSource(final Customer customer) throws IllegalArgumentException, StripeException;

    /**
     * Charges a given amount of the customer,
     *
     * TODO probably use Money API for the amount
     *
     * @param amount the amount to charge
     * @param customer the customer to charge
     * @param source the source of which the charge is going to be used.
     * @return the created charge which can be further processed.
     * @throws IllegalArgumentException in case amount, customer or source is invalid.
     */
    Charge chargeAmount(final float amount, final Customer customer, final Source source)
            throws IllegalArgumentException, StripeException;

    Charge captureCharge(final Charge charge) throws IllegalArgumentException, StripeException;

    Refund refundCharge(final Charge charge) throws IllegalArgumentException, StripeException;
}
