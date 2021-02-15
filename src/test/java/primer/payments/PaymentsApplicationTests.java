package primer.payments;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import primer.payments.api.TokensController;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PaymentsApplicationTests {

    @Autowired
    TokensController tokensController;

    @Test
    void contextLoads() {
        assertThat(tokensController).isNotNull();
    }

}
