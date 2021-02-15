package primer.payments.client;

import primer.payments.model.Payment;
import primer.payments.model.PaymentOutcome;

/*
 * Interface to generify a payment processor - the input and output is designed to prevent a particular
 * processor from leaking into the domain. This contains the work of adding additional connections.
 */
public interface PaymentProcessorClient {
    PaymentOutcome sale(Payment payment) throws PaymentProcessorError;
}
