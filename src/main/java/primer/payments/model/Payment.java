package primer.payments.model;

import java.util.Optional;

public record Payment(int amount, Token token, Optional<String> processor) {
}
