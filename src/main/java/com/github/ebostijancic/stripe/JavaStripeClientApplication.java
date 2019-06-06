package com.github.ebostijancic.stripe;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JavaStripeClientApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(JavaStripeClientApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
