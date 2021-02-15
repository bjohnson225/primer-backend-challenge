package primer.payments.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import primer.payments.client.PaymentProcessorError;
import primer.payments.model.PaymentOutcome;
import primer.payments.model.Token;
import primer.payments.service.PaymentsService;
import primer.payments.service.TokenizationService;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static primer.payments.model.PaymentOutcome.Outcomes.SENT_FOR_SETTLEMENT;

@WebMvcTest(PaymentsController.class)
public class PaymentsControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private TokenizationService tokenizationService;
    @MockBean
    private PaymentsService paymentsService;

    @Test
    public void return201WhenPaymentIsCreated() throws Exception {
        Token token = new Token("4444333322221111", "someValue", "12", "2022");
        when(tokenizationService.getTokenById(any())).thenReturn(Optional.of(token));
        when(paymentsService.makePayment(any())).thenReturn(new PaymentOutcome(SENT_FOR_SETTLEMENT, 100, "EUR", new PaymentOutcome.Card("1111", "visa")));

        mvc.perform(post("/payments").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"amount":1001,"tokenId":"bd5ef426-4fea-4f86-a21c-cad88868b7bc"}
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.outcome", is("Sent for Settlement")))
                .andExpect(jsonPath("$.transactionValue.amount", is(100)))
                .andExpect(jsonPath("$.transactionValue.currency", is("EUR")))
                .andExpect(jsonPath("$.cardDetails.last4", is("1111")))
                .andExpect(jsonPath("$.cardDetails.scheme", is("visa")));
    }

    @Test
    public void return400WhenTokenIsNotFound() throws Exception {
        when(tokenizationService.getTokenById(any())).thenReturn(Optional.empty());

        mvc.perform(post("/payments").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"amount":1001,"tokenId":"bd5ef426-4fea-4f86-a21c-cad88868b7bc"}
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void return500WhenGenericPaymentProcessingErrorOccurs() throws Exception {
        Token token = new Token("4444333322221111", "someValue", "12", "2022");
        when(tokenizationService.getTokenById(any())).thenReturn(Optional.of(token));
        when(paymentsService.makePayment(any())).thenThrow(new PaymentProcessorError("some error"));

        mvc.perform(post("/payments").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"amount":1001,"tokenId":"bd5ef426-4fea-4f86-a21c-cad88868b7bc"}
                        """))
                .andExpect(status().isInternalServerError());
    }
}