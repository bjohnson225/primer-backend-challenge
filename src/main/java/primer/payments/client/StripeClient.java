package primer.payments.client;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import primer.payments.model.Payment;
import primer.payments.model.PaymentOutcome;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static primer.payments.model.PaymentOutcome.Outcomes.DECLINED;
import static primer.payments.model.PaymentOutcome.Outcomes.SENT_FOR_SETTLEMENT;


@Component
public class StripeClient implements PaymentProcessorClient {

    @Autowired
    public StripeClient() {
        // Test Keys only. This would need to be injected & not checked in before getting a prod key.
        Stripe.apiKey = "sk_test_51IJKq1EKVpHnSiMfF14mfjHT4k2JRusKtfVfM9m4FkDP8G1QWMhv6URqKT3veTon9xTj8UXZZOjXvr7rv7V4c70U00bFAjKz9f";
    }

    @Override
    public PaymentOutcome sale(Payment payment) throws PaymentProcessorError {
        try {
            PaymentMethod paymentMethod = createCard(payment);
            PaymentIntent paymentIntent = createPayment(payment, paymentMethod);

            return getPaymentOutcome(paymentMethod, paymentIntent);
        } catch (StripeException e) {
            throw new PaymentProcessorError("Error from Stripe when making payment");
        }
    }

    private PaymentMethod createCard(Payment payment) throws StripeException {
        // Create card object
        Map<String, Object> card = new HashMap<>();
        card.put("number", payment.token().getCardNumber());
        card.put("exp_month", payment.token().getExpiryMonth());
        card.put("exp_year", payment.token().getExpiryYear());
        Map<String, Object> params = new HashMap<>();
        params.put("type", "card");
        params.put("card", card);

        return PaymentMethod.create(params);
    }

    private PaymentIntent createPayment(Payment payment, PaymentMethod paymentMethod) throws StripeException {
        // Create payment intent
        List<String> paymentMethodTypes = Collections.singletonList("card");
        Map<String, Object> intentParams = new HashMap<>();
        intentParams.put("amount", payment.amount());
        intentParams.put("currency", "EUR");
        intentParams.put("payment_method_types", paymentMethodTypes);
        intentParams.put("payment_method", paymentMethod.getId());
        intentParams.put("confirm", true);

        return PaymentIntent.create(intentParams);
    }

    private PaymentOutcome getPaymentOutcome(PaymentMethod paymentMethod, PaymentIntent paymentIntent) {
        PaymentOutcome.Outcomes outcome;
        if (paymentIntent.getStatus().equals("succeeded")) {
            outcome = SENT_FOR_SETTLEMENT;
        } else {
            outcome = DECLINED;
        }

        return new PaymentOutcome(outcome,
                paymentIntent.getAmount().intValue(),
                paymentIntent.getCurrency(),
                new PaymentOutcome.Card(paymentMethod.getCard().getLast4(), paymentMethod.getCard().getBrand()));
    }
}

