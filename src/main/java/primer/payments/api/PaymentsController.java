package primer.payments.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import primer.payments.client.PaymentProcessorError;
import primer.payments.model.Payment;
import primer.payments.model.PaymentOutcome;
import primer.payments.model.dto.request.PaymentsRequestDto;
import primer.payments.model.dto.response.PaymentsResponseDto;
import primer.payments.service.PaymentsService;
import primer.payments.service.TokenizationService;

import java.util.Optional;

import static primer.payments.model.dto.response.PaymentsResponseDto.CardDetails;
import static primer.payments.model.dto.response.PaymentsResponseDto.TransactionValue;

@RestController
@RequestMapping("/payments")
public class PaymentsController {

    private final TokenizationService tokenizationService;
    private final PaymentsService paymentsService;

    @Autowired
    public PaymentsController(TokenizationService tokenizationService,
                              PaymentsService paymentsService) {
        this.tokenizationService = tokenizationService;
        this.paymentsService = paymentsService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentsResponseDto payWithToken(@RequestParam Optional<String> processor, @RequestBody PaymentsRequestDto paymentsRequestDto) {
        return tokenizationService.getTokenById(paymentsRequestDto.getTokenId())
                .map(t -> new Payment(paymentsRequestDto.amount(), t, processor))
                .map(this::makePayment)
                .map(o -> new PaymentsResponseDto(o.outcome().getValue(),
                        new TransactionValue(o.amount(), o.currency()),
                        new CardDetails(o.cardDetails().last4(), o.cardDetails().scheme())))
                .orElseThrow(InvalidTokenId::new);
    }

    private PaymentOutcome makePayment(Payment payment) {
        try {
            return paymentsService.makePayment(payment);
        } catch (PaymentProcessorError e) {
            throw new PaymentProcessingError();
        }
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid Token ID")
    public static class InvalidTokenId extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Unable to process payment due to an error with a Payment provider")
    public static class PaymentProcessingError extends RuntimeException {
    }
}
