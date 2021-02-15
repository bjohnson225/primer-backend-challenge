package primer.payments.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import primer.payments.client.BraintreeClient;
import primer.payments.client.PaymentProcessorError;
import primer.payments.client.StripeClient;
import primer.payments.model.Payment;
import primer.payments.model.PaymentOutcome;

@Service
public class PaymentsService {

    private final BraintreeClient braintreeClient;
    private final StripeClient stripeClient;

    @Autowired
    public PaymentsService(BraintreeClient braintreeClient, StripeClient stripeClient) {
        this.braintreeClient = braintreeClient;
        this.stripeClient = stripeClient;
    }

    public PaymentOutcome makePayment(Payment payment) throws PaymentProcessorError {
        String processor = payment.processor().map(String::toLowerCase).orElse("braintree");

        if (processor.equals("stripe")) {
            return stripeClient.sale(payment);
        } else {
            return braintreeClient.sale(payment);
        }
    }
}
