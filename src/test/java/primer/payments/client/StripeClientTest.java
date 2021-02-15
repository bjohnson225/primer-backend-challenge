package primer.payments.client;

import org.junit.jupiter.api.Test;
import primer.payments.model.Payment;
import primer.payments.model.PaymentOutcome;
import primer.payments.model.Token;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static primer.payments.model.PaymentOutcome.Outcomes.SENT_FOR_SETTLEMENT;

public class StripeClientTest {

    private final StripeClient stripeClient = new StripeClient();

    @Test
    public void authorizePaymentFor1000EUR() throws Exception {
        PaymentOutcome result = stripeClient.sale(new Payment(1000, new Token("4242424242424242", "name", "12", "2022"), Optional.empty()));

        assertThat(result.outcome()).isEqualTo(SENT_FOR_SETTLEMENT);
        assertThat(result.amount()).isEqualTo(1000);
    }

    @Test
    public void authorizePaymentAndReturnCardDetails() throws Exception {
        String expectedLast4 = "4242";
        String expectedScheme = "visa";

        PaymentOutcome result = stripeClient.sale(new Payment(12345, new Token("4242424242424242", "Some Name", "12", "2022"), Optional.empty()));

        assertThat(result.outcome()).isEqualTo(SENT_FOR_SETTLEMENT);
        assertThat(result.cardDetails().last4()).isEqualTo(expectedLast4);
        assertThat(result.cardDetails().scheme()).isEqualTo(expectedScheme);
    }

    @Test
    public void throwGenericErrorWhenStripeErrors() {
        Exception exception = assertThrows(PaymentProcessorError.class, () ->
                stripeClient.sale(new Payment(12345, new Token("4444333322221111", "Some Name", "12", "2022"), Optional.empty())));

        String expectedMessage = "Error from Stripe when making payment";
        assertThat(exception.getMessage()).isEqualTo(expectedMessage);
    }
}
