package primer.payments.client;

import primer.payments.model.Payment;
import primer.payments.model.PaymentOutcome;

public interface PaymentProcessorClient {
    PaymentOutcome sale(Payment payment) throws PaymentProcessorError;
}
