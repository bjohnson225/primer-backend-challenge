package primer.payments.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import primer.payments.client.BraintreeClient;
import primer.payments.client.StripeClient;
import primer.payments.model.Payment;
import primer.payments.model.PaymentOutcome;
import primer.payments.model.Token;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static primer.payments.model.PaymentOutcome.Outcomes.SENT_FOR_SETTLEMENT;

@SpringBootTest
public class PaymentsServiceTest {
    private final PaymentOutcome genericOutcome = new PaymentOutcome(SENT_FOR_SETTLEMENT, 100, "EUR", new PaymentOutcome.Card("1111", "visa"));
    @Autowired
    PaymentsService paymentsService;
    @MockBean
    BraintreeClient braintreeClient;
    @MockBean
    StripeClient stripeClient;

    @Test
    public void shouldProcessPaymentWithBraintreeByDefault() throws Exception {
        when(braintreeClient.sale(any())).thenReturn(genericOutcome);

        PaymentOutcome result = paymentsService.makePayment(new Payment(100, new Token("44443333222221111", "name", "12", "2022"), Optional.empty()));

        assertThat(result.outcome()).isEqualTo(SENT_FOR_SETTLEMENT);
    }

    @Test
    public void shouldProcessPaymentWithStripeWhenSelected() throws Exception {
        when(stripeClient.sale(any())).thenReturn(genericOutcome);

        PaymentOutcome result = paymentsService.makePayment(new Payment(100, new Token("44443333222221111", "name", "12", "2022"), Optional.of("Stripe")));

        assertThat(result.outcome()).isEqualTo(SENT_FOR_SETTLEMENT);
    }

    @Test
    public void shouldProcessPaymentWithBraintreeWhenSelected() throws Exception {
        when(braintreeClient.sale(any())).thenReturn(genericOutcome);

        PaymentOutcome result = paymentsService.makePayment(new Payment(100, new Token("44443333222221111", "name", "12", "2022"), Optional.of("Braintree")));

        assertThat(result.outcome()).isEqualTo(SENT_FOR_SETTLEMENT);
    }
}
