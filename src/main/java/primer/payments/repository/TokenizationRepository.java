package primer.payments.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import primer.payments.model.Token;

import java.util.UUID;

@Repository
public interface TokenizationRepository extends CrudRepository<Token, UUID> {
    // Method to save and get Entity auto-created by extending CrudRepository
}
