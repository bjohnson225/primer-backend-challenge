package primer.payments.client;

import org.junit.jupiter.api.Test;
import primer.payments.model.Payment;
import primer.payments.model.PaymentOutcome;
import primer.payments.model.Token;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static primer.payments.model.PaymentOutcome.Outcomes.DECLINED;
import static primer.payments.model.PaymentOutcome.Outcomes.SENT_FOR_SETTLEMENT;

public class BraintreeClientTest {

    private final BraintreeClient braintreeClient = new BraintreeClient();

    @Test
    public void authorizePaymentFor1234EUR() throws Exception {
        int expectedAmount = 1234;
        String expectedCurrency = "EUR";

        PaymentOutcome result = braintreeClient.sale(new Payment(1234, new Token("4444333322221111", "Some Name", "12", "2022"), Optional.empty()));

        assertThat(result.outcome()).isEqualTo(SENT_FOR_SETTLEMENT);
        assertThat(result.amount()).isEqualTo(expectedAmount);
        assertThat(result.currency()).isEqualTo(expectedCurrency);
    }

    @Test
    public void declineAPayment() throws Exception {
        int amountWhichDeclines = 200000;
        PaymentOutcome result = braintreeClient.sale(new Payment(amountWhichDeclines, new Token("4444333322221111", "Some Name", "12", "2022"), Optional.empty()));

        assertThat(result.outcome()).isEqualTo(DECLINED);
    }

    @Test
    public void authorizePaymentAndReturnCardDetails() throws Exception {
        String expectedLast4 = "1111";
        String expectedScheme = "visa";

        PaymentOutcome result = braintreeClient.sale(new Payment(12345, new Token("4444333322221111", "Some Name", "12", "2022"), Optional.empty()));

        assertThat(result.outcome()).isEqualTo(SENT_FOR_SETTLEMENT);
        assertThat(result.cardDetails().last4()).isEqualTo(expectedLast4);
        assertThat(result.cardDetails().scheme()).isEqualTo(expectedScheme);
    }

    @Test
    public void throwGenericErrorWhenBraintreeErrors() {
        Exception exception = assertThrows(PaymentProcessorError.class, () ->
                braintreeClient.sale(new Payment(12345, new Token("44443333222211110000", "Some Name", "12", "2022"), Optional.empty())));

        String expectedMessage = "Error from Braintree when making payment";
        assertThat(exception.getMessage()).isEqualTo(expectedMessage);
    }
}
