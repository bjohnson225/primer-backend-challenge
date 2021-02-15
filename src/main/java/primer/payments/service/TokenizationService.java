package primer.payments.service;

import org.springframework.stereotype.Service;
import primer.payments.model.Token;
import primer.payments.model.dto.request.TokenRequestDto;
import primer.payments.repository.TokenizationRepository;

import java.util.Optional;
import java.util.UUID;

/*
 * Tokenization Service allows for the creation and retrieval of tokens
 */
@Service
public class TokenizationService {

    private final TokenizationRepository tokenizationRepository;

    public TokenizationService(TokenizationRepository tokenizationRepository) {
        this.tokenizationRepository = tokenizationRepository;
    }

    public Optional<Token> getTokenById(String id) {
        return tokenizationRepository.findById(UUID.fromString(id));
    }

    public String create(TokenRequestDto tokenRequest) {
        Token token = new Token(tokenRequest.cardNumber(),
                tokenRequest.cardHolderName(),
                tokenRequest.expiryDate().month(),
                tokenRequest.expiryDate().year());

        Token savedToken = tokenizationRepository.save(token);
        return savedToken.getId().toString();
    }
}
