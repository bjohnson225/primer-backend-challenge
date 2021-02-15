package primer.payments.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import primer.payments.model.Token;
import primer.payments.model.dto.request.ExpiryDateDto;
import primer.payments.model.dto.request.TokenRequestDto;
import primer.payments.repository.TokenizationRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TokenizationServiceTest {
    @Autowired
    TokenizationRepository tokenizationRepository;
    @Autowired
    TokenizationService tokenizationService;

    @Test
    public void shouldGetAnExistingToken() {
        String savedId = tokenizationRepository.save(new Token("4444333322221111", "Some Name", "01", "2023")).getId().toString();

        Token result = tokenizationService.getTokenById(savedId).get();

        assertThat(result.getCardNumber()).isEqualTo("4444333322221111");
        assertThat(result.getCardHolderName()).isEqualTo("Some Name");
        assertThat(result.getExpiryMonth()).isEqualTo("01");
        assertThat(result.getExpiryYear()).isEqualTo("2023");
    }

    @Test
    public void shouldCreateToken() {
        TokenRequestDto someRequest = new TokenRequestDto("44433332222111", "name", new ExpiryDateDto("01", "2022"));

        String createdId = tokenizationService.create(someRequest);

        assertThat(createdId).isNotBlank();
    }

}
