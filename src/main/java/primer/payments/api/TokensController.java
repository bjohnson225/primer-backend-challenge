package primer.payments.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import primer.payments.model.dto.request.TokenRequestDto;
import primer.payments.model.dto.response.TokenResponseDto;
import primer.payments.service.TokenizationService;

@RestController
@RequestMapping("/tokens")
public class TokensController {

    private final TokenizationService tokenizationService;

    @Autowired
    public TokensController(TokenizationService tokenizationService) {
        this.tokenizationService = tokenizationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponseDto createToken(@RequestBody TokenRequestDto tokenRequestDto) {
        return new TokenResponseDto(tokenizationService.create(tokenRequestDto));
    }
}
