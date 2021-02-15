package primer.payments.model;

import java.util.Optional;

/*
 * Domain level representation of a payment
 */
public record Payment(int amount, Token token, Optional<String> processor) {
}
