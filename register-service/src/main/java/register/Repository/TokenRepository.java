package register.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import org.springframework.stereotype.Repository;
import register.Model.Token;

/**
 * Created by eduard on 18/05/16.
 */

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Token findByUserIdAndId(String userId, Long id);

    List<Token> findByUserId(String userId);
}