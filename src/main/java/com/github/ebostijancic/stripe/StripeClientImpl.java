package com.github.ebostijancic.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Source;
import com.stripe.net.RequestOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class StripeClientImpl implements StripeClient {

    private final RequestOptions requestOptions;

    // Stripe's minimal amount to charge a customer.
    private final static float MINIMAL_AMOUNT = 0.5f;

    private static final String DEFAULT_TOKEN = "tok_at";

    @Value("${stripe.api_key}")
    private String stripeApiKey;

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
    public Customer addCustomer(final String email) throws IllegalArgumentException, StripeException {
        if (StringUtils.isEmpty(email)) {
            throw new IllegalArgumentException("No email for customer given");
        }

        final Map<String, Object> params = new HashMap<>();
        params.put("email", email);

        return Customer.create(params, requestOptions);
    }

    @Override
    public Source attachCreditCardSource(final Customer customer) throws IllegalArgumentException, StripeException {

        if (customer == null || StringUtils.isEmpty(customer.getEmail())) {
            throw new IllegalArgumentException("No valid customer given");
        }

        final Map<String, Object> owner = new HashMap<>();
        owner.put("email", customer.getEmail());

        final Map<String, Object> sourceParams = new HashMap<>();
        sourceParams.put("type", "card");
        sourceParams.put("token", DEFAULT_TOKEN);
        sourceParams.put("owner", owner);

        return Source.create(sourceParams, this.requestOptions);
    }


    @Override
    public Charge chargeAmount(final float amount, final Customer customer, final Source source)
            throws IllegalArgumentException, StripeException {

        if (amount == 0.0f) {
            throw new IllegalArgumentException("No amount to charge given.");
        }

        if (amount < MINIMAL_AMOUNT) {
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

        return Charge.create(params, requestOptions);
    }
}
