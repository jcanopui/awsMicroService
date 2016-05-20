package push;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
        //SpringApplicationBuilder(Application.class).web(false).run(args);
    }
}

@Component
class DiscoveryClientExample implements CommandLineRunner {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    public void run(String... strings) throws Exception {
        discoveryClient.getInstances("register-service").forEach((ServiceInstance s) -> {
            System.out.println(ToStringBuilder.reflectionToString(s));
        });
    }
}

@Component
class RestTemplateExample implements CommandLineRunner {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void run(String... strings) throws Exception {
        // use the "smart" Eureka-aware RestTemplate
        ResponseEntity<List<Token>> exchange =
                this.restTemplate.exchange(
                        "http://register-service/{userId}/tokens",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Token>>() {
                        },
                        (Object) "user1");

        exchange.getBody().forEach(System.out::println);
    }

}

@Component
class FeignExample implements CommandLineRunner {

    @Autowired
    private PushClient pushClient;

    @Override
    public void run(String... strings) throws Exception {
        this.pushClient.getTokens("user1").forEach(System.out::println);
    }
}

@FeignClient("register-service")
interface PushClient {

    @RequestMapping(method = RequestMethod.GET, value = "/{userId}/tokens")
    List<Token> getTokens(@PathVariable("userId") String userId);
}

class Token {
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

