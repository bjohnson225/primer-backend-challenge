package primer.payments.model.dto.response;

/*
 * Class used by Jackson to serialize payment response to JSON
 */
public record PaymentsResponseDto(String outcome, TransactionValue transactionValue, CardDetails cardDetails) {

    public record TransactionValue(int amount, String currency) {
    }

    public record CardDetails(String last4, String scheme) {
    }
}
