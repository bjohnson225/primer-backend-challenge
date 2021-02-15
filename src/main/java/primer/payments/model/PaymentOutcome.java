package primer.payments.model;

/*
 * Domain level representation of a payment outcome
 */
public record PaymentOutcome(Outcomes outcome, int amount, String currency, Card cardDetails) {
    public enum Outcomes {
        SENT_FOR_SETTLEMENT("Sent for Settlement"), DECLINED("Declined");

        private final String value;

        Outcomes(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public record Card(String last4, String scheme) {
    }
}

