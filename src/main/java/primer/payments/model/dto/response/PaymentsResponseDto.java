package primer.payments.model.dto.response;

public record PaymentsResponseDto(String outcome, TransactionValue transactionValue, CardDetails cardDetails) {

    public record TransactionValue(int amount, String currency) {
    }

    public record CardDetails(String last4, String scheme) {
    }
}
