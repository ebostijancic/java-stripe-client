package com.github.ebostijancic.stripe;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class Wizard {
    private StripeClient client;

    public Wizard(@Autowired StripeClient client) {
        this.client = client;
    }

    /**
     * Reads the email address of the customer from System.in and creates
     * a new Customer at Stripe. In case an invalid email is given, it will repeat
     * the input prompt until the given email address is valid.
     *
     * @return Customer or null in case it did not work.
     */
    public Customer addCustomer() {
        System.out.println("Please enter email of customer");
        String email;

        final Scanner scanner = new Scanner(System.in);

        do {
            email = scanner.nextLine();

            if (StripeUtil.isValidEmail(email) == false) {
                System.err.println("Email of customer is invalid, please enter a valid email address");
            }
        } while (StripeUtil.isValidEmail(email) == false);

        try {
            return this.client.addCustomer(email);
        } catch (IllegalArgumentException | StripeException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Waits for the charge input from System.in and in case the given amount is
     * valid, it will charge the given customer.
     *
     * @param customer
     * @return
     */
    public Charge chargeCustomer(final Customer customer) {
        System.out.println("Please enter amount in EUR to charge the customer");

        final Scanner scanner = new Scanner(System.in);
        float amount;

        do {
            amount = scanner.nextFloat();
            if (StripeUtil.isValidAmount(amount) == false) {
                System.err.println("Given amount is invalid, please enter amount higher than 0.5 EUR");
            }
        } while(StripeUtil.isValidAmount(amount) == false);

        try {
            final Source source = client.attachCreditCardSource(customer);
            final Charge charge = client.chargeAmount(amount, customer, source);
            return charge;

        } catch (Exception e) {
            System.out.println("Error charging customer");
            e.printStackTrace();
        }

        return null;
    }

    public int refundOrCapture(final Charge charge) {
        System.out.println("Do you want to capture or refund the charge? (C/R)");

        final Scanner scanner = new Scanner(System.in);

        String answer;

        do {
            answer = scanner.nextLine();

            if (StripeUtil.isValidAnswer(answer) == false) {
                System.err.println("Given answer is invalid, use only one of the following inputs (c|r|capture|refund)");
            }
        } while(StripeUtil.isValidAnswer(answer) == false);


        return -1;
    }
}