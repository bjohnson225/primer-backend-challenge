package primer.payments.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

/*
 * Domain layer representation of a token that is also used to save a token to the database
 */
@Entity
public class Token {
    @Id
    @GeneratedValue
    private UUID id;
    private String cardNumber;
    private String cardHolderName;
    private String expiryMonth;
    private String expiryYear;

    public Token() {
    }

    public Token(String cardNumber, String cardHolderName, String expiryMonth, String expiryYear) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
    }

    public UUID getId() {
        return id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

}
