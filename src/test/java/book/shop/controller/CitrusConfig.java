package book.shop.controller;

import java.util.Objects;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.http.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class CitrusConfig {

    private final Environment environment;

    public CitrusConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public HttpClient bookClient() {
        int port = Integer.parseInt(Objects.requireNonNull(environment.getProperty("local.server.port")));
        return new HttpClientBuilder()
                .requestUrl("http://localhost:" + port) // Define the base URL for your HttpClient
                .build();
    }
}
