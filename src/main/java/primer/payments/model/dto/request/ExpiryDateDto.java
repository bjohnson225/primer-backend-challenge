package primer.payments.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExpiryDateDto(@JsonProperty(value = "month", required = true) String month,
                            @JsonProperty(value = "year", required = true) String year) {

}
