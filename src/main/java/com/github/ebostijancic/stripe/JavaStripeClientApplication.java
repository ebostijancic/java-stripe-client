package com.github.ebostijancic.stripe;

import com.stripe.model.Charge;
import com.stripe.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JavaStripeClientApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(JavaStripeClientApplication.class, args);
	}

	@Autowired
	private StripeClient client;

	@Override
	public void run(String... args) throws Exception {
		final Wizard wizard = new Wizard(client);

		final Customer customer = wizard.addCustomer();

		if (customer == null) {
			System.exit(1);
		}

		final Charge charge = wizard.chargeCustomer(customer);

		if (charge == null) {
			System.exit(2);
		}

		System.exit(wizard.refundOrCapture(charge));
		return;
	}
}

