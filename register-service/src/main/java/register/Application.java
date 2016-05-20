package register;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import register.Model.Token;
import register.Repository.TokenRepository;


@SpringBootApplication
@EnableEurekaClient
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner init(TokenRepository tokenRepository) {
        return args -> {
            tokenRepository.deleteAll();

            /*Arrays.asList("user1", "user2").forEach(n ->
                    tokenRepository.save(new Token(n)));*/
        };
    }
}
