package push.Model;

/**
 * Created by eduard on 20/05/16.
 */

public class Token {
    private Long id;
    private String userId;

    @Override
    public String toString() {
        return "Token { " + "id=" + id + ", userId='" + userId + '\'' + " }";
    }

    public Token() {
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }
}