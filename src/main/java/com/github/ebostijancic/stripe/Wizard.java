package com.github.ebostijancic.stripe;

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

    public Customer addCustomer() {
        System.out.println("Please enter email of customer");
        String email;

        final Scanner scanner = new Scanner(System.in);

        do {
            email = scanner.nextLine();

            if (StripeUtil.isValidEmail(email) == false) {
                System.out.println("Email of customer is invalid, please enter a valid email address");
            }
        } while (StripeUtil.isValidEmail(email) == false);

        try {
            return this.client.addCustomer(email);
        } catch (IllegalArgumentException | StripeException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Charge chargeCustomer(final Customer customer) {
        System.out.println("Please enter amount in EUR to charge the customer");

        final Scanner scanner = new Scanner(System.in);
        float amount;

        do {
            amount = scanner.nextFloat();
            if (StripeUtil.isValidAmount(amount) == false) {
                System.out.println("Given amount is invalid, please enter amount higher than 0.5 EUR");
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


        return -1;
    }

}