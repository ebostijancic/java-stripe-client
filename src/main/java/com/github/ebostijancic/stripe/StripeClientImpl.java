package com.github.ebostijancic.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Source;
import com.stripe.net.RequestOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StripeClientImpl implements StripeClient {

    private final RequestOptions requestOptions;

    // Stripe's minimal amount to charge a customer.
    private final static float MINIMAL_AMOUNT = 0.5f;

    @Value("${stripe.api_key}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {

    }

    public StripeClientImpl(@Value("${stripe.api_key}") String stripeApiKey) throws IllegalArgumentException {
        if (StringUtils.isEmpty(stripeApiKey)) {
            throw new IllegalArgumentException("No api key given");
        }

        this.stripeApiKey = stripeApiKey;

        this.requestOptions = RequestOptions.builder()
                .setApiKey(stripeApiKey)
                .build();
    }

    @Override
    public Optional<Customer> addCustomer(final String email) throws IllegalArgumentException {
        if (StringUtils.isEmpty(email)) {
            throw new IllegalArgumentException("No email for customer given");
        }

        final Map<String, Object> params = new HashMap<>();
        params.put("email", email);

        try {
            final Customer customer = Customer.create(params, requestOptions);
            return Optional.of(customer);
        } catch (StripeException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Source> attachCreditCardSource(final Customer customer) throws IllegalArgumentException {

        if (customer == null || StringUtils.isEmpty(customer.getEmail())) {
            throw new IllegalArgumentException("No valid customer given");
        }

        final Map<String, Object> owner = new HashMap<>();
        owner.put("email", customer.getEmail());

        final Map<String, Object> sourceParams = new HashMap<>();
        sourceParams.put("type", "card");
        sourceParams.put("token", "tok_at");
        sourceParams.put("owner", owner);

        try {
            final Source source = Source.create(sourceParams, this.requestOptions);
            return Optional.of(source);
        } catch (StripeException e) {
            return Optional.empty();
        }
    }


    @Override
    public boolean chargeAmount(final Float amount, final Customer customer, final Source source)
            throws IllegalArgumentException {

        if (amount == null || amount.floatValue() == 0.0f) {
            throw new IllegalArgumentException("No or zero amount to charge given.");
        }

        if (amount.floatValue() < MINIMAL_AMOUNT) {
            throw new IllegalArgumentException("Given amount is below the minimal allowed charge amount");
        }

        if (customer == null || StringUtils.isEmpty(customer.getId())) {
            throw new IllegalArgumentException("Invalid customer given");
        }

        final int chargeAmount = StripeUtil.getChargeInCents(amount);

        if (source == null) {
            throw new IllegalArgumentException("No source given");
        }

        final Map<String, Object> params = new HashMap<>();
        params.put("amount", chargeAmount);
        params.put("source", source.getId());
        params.put("currency", "eur");

        try {
            Charge charge = Charge.create(params, requestOptions);
            return true;
        } catch (StripeException e) {
            e.getStripeError();
        }

        return false;
    }
}
