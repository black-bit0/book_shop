package book.shop.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestWebConfig {

    @Value("${local.server.port}")
    private int port;

    public int getPort() {
        return port;
    }

    public String getBaseUri() {
        return "http://localhost:" + port;
    }
}
