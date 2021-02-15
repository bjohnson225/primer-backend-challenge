package primer.payments.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenRequestDto(@JsonProperty(value = "cardHolderName", required = true) String cardHolderName,
                              @JsonProperty(value = "cardNumber", required = true) String cardNumber,
                              @JsonProperty(value = "expiryDate", required = true) ExpiryDateDto expiryDate) {

}
