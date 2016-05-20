package register.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by eduard on 18/05/16.
 */

@Entity
public class Token {

    private String userId;

    @Id
    @GeneratedValue
    private Long id;

    Token() {
    }

    public Token(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public Long getId() {
        return id;
    }
}