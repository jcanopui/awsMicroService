package push.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by eduard on 20/05/16.
 */

@Component
class FeignExample implements CommandLineRunner {

    @Autowired
    private PushClient pushClient;

    @Override
    public void run(String... strings) throws Exception {
        this.pushClient.getTokens("user2").forEach(System.out::println);
    }
}
