package primer.payments.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record PaymentsRequestDto(@JsonProperty(value = "amount", required = true) int amount,
                                 @JsonProperty(value = "tokenId", required = true) UUID tokenId) {
    public String getTokenId() {
        return tokenId.toString();
    }
}
