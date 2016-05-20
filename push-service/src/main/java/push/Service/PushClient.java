package push.Service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import push.Model.Token;

import java.util.List;

/**
 * Created by eduard on 20/05/16.
 */

@FeignClient("register-service")
interface PushClient {

    @RequestMapping(method = RequestMethod.GET, value = "/{userId}/tokens")
    List<Token> getTokens(@PathVariable("userId") String userId);
}