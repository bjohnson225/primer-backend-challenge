package primer.payments.client;

public class PaymentProcessorError extends Exception {
    public PaymentProcessorError(String message) {
        super(message);
    }
}
