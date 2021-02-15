package primer.payments.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Jackson class to deserialize a token creation request into an object
 */
public record TokenRequestDto(@JsonProperty(value = "cardHolderName", required = true) String cardHolderName,
                              @JsonProperty(value = "cardNumber", required = true) String cardNumber,
                              @JsonProperty(value = "expiryDate", required = true) ExpiryDateDto expiryDate) {

    public record ExpiryDateDto(@JsonProperty(value = "month", required = true) String month,
                                @JsonProperty(value = "year", required = true) String year) {

    }
}
