package push.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import push.Model.Token;

import java.util.List;

@RestController
@RequestMapping("/{userId}/push")
public class PushRestController {

    @Autowired
    private PushClient pushClient;

    @RequestMapping(value = "/{message}", method = RequestMethod.GET)
    boolean sendPush(@PathVariable String userId,
                   @PathVariable String message) {

        @SuppressWarnings("unused")
		List<Token> tokenList = pushClient.getTokens(userId);
        
        return false;
    }
}
