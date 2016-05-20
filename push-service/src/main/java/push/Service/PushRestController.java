package push.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import push.Model.Token;

import java.util.List;

/**
 * Created by eduard on 20/05/16.
 */

@RestController
@RequestMapping("/{userId}/push")
public class PushRestController {

    @Autowired
    private PushClient pushClient;

    @RequestMapping(method = RequestMethod.POST)
    boolean sendPush(@PathVariable String userId,
                   @PathVariable String message) {

        List<Token> tokenList = pushClient.getTokens(userId);
        
        return false;
    }
}
