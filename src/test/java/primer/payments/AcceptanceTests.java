package primer.payments;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import primer.payments.model.dto.request.TokenRequestDto.ExpiryDateDto;
import primer.payments.model.dto.request.PaymentsRequestDto;
import primer.payments.model.dto.request.TokenRequestDto;
import primer.payments.model.dto.response.PaymentsResponseDto;
import primer.payments.model.dto.response.TokenResponseDto;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTests {
    @LocalServerPort
    private int port;
    TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void shouldCreateTokenAndThenMakePaymentWithBraintree() {
        // Create a token
        HttpEntity<TokenRequestDto> tokenRequest = new HttpEntity<>(new TokenRequestDto("Some Name", "4444333322221111", new ExpiryDateDto("01", "2022")));
        TokenResponseDto tokenResult = restTemplate.postForObject(createURLWithPort("/tokens"), tokenRequest, TokenResponseDto.class);

        assertThat(tokenResult.tokenId()).isNotBlank();

        // Use the token with Braintree (default)
        HttpEntity<PaymentsRequestDto> paymentRequest = new HttpEntity<>(new PaymentsRequestDto(1000, UUID.fromString(tokenResult.tokenId())));
        PaymentsResponseDto paymentResult = restTemplate.postForObject(createURLWithPort("/payments"), paymentRequest, PaymentsResponseDto.class);

        assertThat(paymentResult.outcome()).isEqualTo("Sent for Settlement");
    }

    @Test
    public void shouldCreateTokenAndThenMakePaymentWithStripe() {
        // Create a token
        HttpEntity<TokenRequestDto> tokenRequest = new HttpEntity<>(new TokenRequestDto("Some Name", "4242424242424242 ", new ExpiryDateDto("01", "2022")));
        TokenResponseDto tokenResult = restTemplate.postForObject(createURLWithPort("/tokens"), tokenRequest, TokenResponseDto.class);

        assertThat(tokenResult.tokenId()).isNotBlank();

        // Use the token with Stripe
        HttpEntity<PaymentsRequestDto> paymentRequest = new HttpEntity<>(new PaymentsRequestDto(1000, UUID.fromString(tokenResult.tokenId())));
        PaymentsResponseDto paymentResult = restTemplate.postForObject(createURLWithPort("/payments?processor=stripe"), paymentRequest, PaymentsResponseDto.class);

        assertThat(paymentResult.outcome()).isEqualTo("Sent for Settlement");
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
