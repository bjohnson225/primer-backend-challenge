package primer.payments.client;

import com.braintreegateway.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import primer.payments.model.Payment;
import primer.payments.model.PaymentOutcome;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import static com.braintreegateway.Transaction.Status.SUBMITTED_FOR_SETTLEMENT;
import static primer.payments.model.PaymentOutcome.Card;
import static primer.payments.model.PaymentOutcome.Outcomes;
import static primer.payments.model.PaymentOutcome.Outcomes.DECLINED;
import static primer.payments.model.PaymentOutcome.Outcomes.SENT_FOR_SETTLEMENT;

@Component
public class BraintreeClient implements PaymentProcessorClient {
    private static BraintreeGateway gateway;

    @Autowired
    public BraintreeClient() {
        // Test Keys only. This would need to be injected & not checked in before getting a prod key.
        gateway = new BraintreeGateway(
                Environment.SANDBOX,
                "hyscfmzsyvjvqm4c",
                "7n56554h4kqt8hy8",
                "8b846af965d55ea244640a0db86222ea"
        );
    }

    @Override
    public PaymentOutcome sale(Payment payment) throws PaymentProcessorError {
        TransactionRequest request = new TransactionRequest()
                .orderId(UUID.randomUUID().toString())
                .amount(BigDecimal.valueOf(payment.amount()).divide(new BigDecimal("100"), 2, RoundingMode.UNNECESSARY))
                .currencyIsoCode("EUR")
                .creditCard()
                .cardholderName(payment.token().getCardHolderName())
                .number(payment.token().getCardNumber())
                .expirationMonth(payment.token().getExpiryMonth())
                .expirationYear(payment.token().getExpiryYear())
                .done()
                .options()
                .submitForSettlement(true)
                .done();

        Result<Transaction> result = gateway.transaction().sale(request);

        if (result.isSuccess()) {
            return convertToDomainModel(result.getTarget());
        } else {
            if (result.getTransaction() != null) {
                return convertToDomainModel(result.getTransaction());
            } else {
                throw new PaymentProcessorError("Error from Braintree when making payment");
            }
        }
    }

    private PaymentOutcome convertToDomainModel(Transaction result) {
        Outcomes outcome;
        if (result.getStatus().equals(SUBMITTED_FOR_SETTLEMENT)) {
            outcome = SENT_FOR_SETTLEMENT;
        } else {
            outcome = DECLINED;
        }

        return new PaymentOutcome(outcome,
                result.getAmount().multiply(new BigDecimal("100")).intValue(),
                result.getCurrencyIsoCode(),
                new Card(result.getCreditCard().getLast4(), result.getCreditCard().getCardType().toLowerCase()));
    }
}

